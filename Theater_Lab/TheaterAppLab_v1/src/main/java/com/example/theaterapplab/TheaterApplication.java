package com.example.theaterapplab;

import com.example.theaterapplab.enums.Section;
import com.example.theaterapplab.model.*;
import com.example.theaterapplab.utils.Utils;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TheaterApplication extends Application {
    private static List<PerformanceTicket> performanceTickets = TheaterManagement.getPerformanceTickets();
    private static PerformanceTicket selectedPerformanceTicket = performanceTickets.get(0);
    private static List<Seat> selectedSeats = new ArrayList<>();
    private static boolean enableUnreserved = false;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(TheaterApplication.class.getResource("theater-view.fxml"));
        stage.setTitle("Theater Lab - Ba Luan Tran - 614224");
        Scene scene = new Scene(new Group());
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setWidth(1430);
        stage.setHeight(1000);

        ((Group) scene.getRoot()).getChildren().addAll(this.getLayout());
        stage.setScene(scene);
        stage.show();

    }

    public Parent getLayout() {
        BorderPane root = new BorderPane();
        root.setId("root");
        TableView<PerformanceProperty> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn nameCol = TheaterApplication.getTableColumn("Performance Name");
        nameCol.setPrefWidth(150);
        nameCol.setCellValueFactory( new PropertyValueFactory<PerformanceProperty, String>("name"));
        TableColumn startTimeCol = TheaterApplication.getTableColumn("Start Time");
        startTimeCol.setCellValueFactory( new PropertyValueFactory<PerformanceProperty, String>("startTime"));
        TableColumn endTimeCol = TheaterApplication.getTableColumn("End Time");
        endTimeCol.setCellValueFactory( new PropertyValueFactory<PerformanceProperty, String>("endTime"));

        TableColumn actionCol = TheaterApplication.getTableColumn("Action");

        HBox hBoxMain = new HBox(3);
        hBoxMain.setSpacing(50);
        hBoxMain.setPadding(new Insets(20, 20, 20, 20));

        Callback<TableColumn<PerformanceProperty, String>, TableCell<PerformanceProperty, String>> cellFactory
                =  new Callback<>() {
            @Override
            public TableCell<PerformanceProperty, String> call(TableColumn<PerformanceProperty, String> tableColumn) {
                final TableCell<PerformanceProperty, String> cell = new TableCell<>() {
                    final Button btn = new Button("Viewing");
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(null);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.setOnAction(event -> {
                                PerformanceProperty performanceProperty = getTableView().getItems().get(getIndex());
                                selectedPerformanceTicket = performanceTickets.stream()
                                        .filter(p -> p.getPerformance().getId() == performanceProperty.getId())
                                        .findAny()
                                        .orElse(new PerformanceTicket());

                                reRenderSeatingChart(hBoxMain);
                            });
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        actionCol.setCellFactory(cellFactory);

        table.getColumns().addAll(nameCol, startTimeCol, endTimeCol, actionCol);
        table.setItems(FXCollections.observableArrayList(
            performanceTickets.stream().map(PerformanceTicket::getPerformance).map(PerformanceProperty::convertToProperty).toList()
        ));

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getChildren().addAll(table);

        VBox vbox1 = renderSeatLayout(selectedPerformanceTicket);

        VBox vbox2 = new VBox();
        vbox2.setSpacing(5);
        vbox2.setAlignment(Pos.TOP_LEFT);
        vbox2.getChildren().addAll(TheaterApplication.createSeatNotes());

        hBoxMain.getChildren().add(0, vbox);
        hBoxMain.getChildren().add(1, vbox1);
        hBoxMain.getChildren().add(2, vbox2);

        // Menu
        MenuBar menubar = new MenuBar();
        Label emptyAllSeatsLbl = new Label("EmptyAllSeats");
        emptyAllSeatsLbl.setOnMouseClicked(mouseEvent-> {
            selectedPerformanceTicket.emptyAllSeats();
            reRenderSeatingChart(hBoxMain);
        });
        Menu emptyAllSeatsMenu = new Menu("", emptyAllSeatsLbl);

        Label reserveAllSeatsLbl = new Label("ReserveAllSeats");
        reserveAllSeatsLbl.setOnMouseClicked(mouseEvent-> {
            selectedPerformanceTicket.reserveAllSeats();
            reRenderSeatingChart(hBoxMain);
        });
        Menu reserveAllSeatsMenu = new Menu("", reserveAllSeatsLbl);

        Label reserveSeatLbl = new Label("ReserveSeat");
        reserveSeatLbl.setOnMouseClicked(mouseEvent-> {
            if (!selectedSeats.isEmpty()) {
                List<String> data = selectedSeats.stream().map(s -> {
                        return String.format("(%s-%s)", s.getRow() + 1 , s.getColumn() + 1);
                    }).toList();

                List<Ticket> tickets = selectedSeats.stream().map(s -> new Ticket(selectedPerformanceTicket.getPerformance(), s)).toList();
                double totalPrices = tickets.stream().mapToDouble(Ticket::getTotalPrice).sum();

                Text actiontarget = new Text();
                actiontarget.setFill(Color.BLUE);
                actiontarget.setText(String.format("Seat: %s%nTotal prices: $%.2f",
                        data.stream().collect(Collectors.joining(", ")),
                        totalPrices));

                Label nameLbl = new Label("Name");
                TextField nameTF = new TextField();

                Label addressLbl = new Label("Address");
                TextField addressTF = new TextField();

                Label phoneLbl = new Label("Phone");
                TextField phoneTF = new TextField();

                GridPane gridPane = createGridPanel();
                gridPane.setAlignment(Pos.CENTER_LEFT);
                gridPane.add(nameLbl, 0, 0);
                gridPane.add(nameTF, 1, 0);
                gridPane.add(addressLbl, 0, 1);
                gridPane.add(addressTF, 1, 1);
                gridPane.add(phoneLbl, 0, 2);
                gridPane.add(phoneTF, 1, 2);
//                gridPane.add(actiontarget, 1, 3);

                VBox vboxContent = new VBox();
                vboxContent.setSpacing(5);
                vboxContent.setAlignment(Pos.CENTER);
                vboxContent.getChildren().addAll(gridPane, actiontarget);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Do you want to reserve Seats as below?");
                alert.getDialogPane().setPrefWidth(400);
                alert.getDialogPane().setContent(vboxContent);

                ButtonType button = alert.showAndWait().orElse(ButtonType.CANCEL);
                if(button == ButtonType.OK) {
                    Person person = new Person(nameTF.getText(), addressTF.getText(), phoneTF.getText());
                    System.out.println("List selected seats: ");
                    selectedSeats.stream().forEach(s -> {
                        selectedPerformanceTicket.reserveSeat(s.getColumn(), s.getRow());
                    });
                    person.addAllTickets(tickets);
                    selectedPerformanceTicket.setTickets(tickets);
                    selectedSeats = new ArrayList<>();
                    reRenderSeatingChart(hBoxMain);
                } else {
                    alert.close();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Please choose seats!!!");
                alert.getDialogPane().setPrefWidth(400);
                alert.show();
            }
        });
        Menu reserveSeatLblMenu = new Menu("", reserveSeatLbl);

        Label unreserveSeatLbl = new Label(String.format("UnreserveSeat (%s)", enableUnreserved ? "active" : "disabled"));
        unreserveSeatLbl.getStyleClass().addAll("unreserveSeatLbl", enableUnreserved ? "active" : "disabled");
        Menu unreserveSeatLblMenu = new Menu("", unreserveSeatLbl);
        unreserveSeatLbl.setOnMouseClicked(mouseEvent-> {
            enableUnreserved = !enableUnreserved;
            if (enableUnreserved) {
                unreserveSeatLbl.getStyleClass().add("active");
                unreserveSeatLbl.getStyleClass().remove("disabled");
                unreserveSeatLbl.setText("UnreserveSeat (active)");
            } else {
                unreserveSeatLbl.getStyleClass().add("disabled");
                unreserveSeatLbl.getStyleClass().remove("active");
                unreserveSeatLbl.setText("UnreserveSeat (disabled)");
            }
        });


        menubar.getMenus().addAll(emptyAllSeatsMenu, reserveAllSeatsMenu, reserveSeatLblMenu, unreserveSeatLblMenu);
        menubar.setPrefWidth(1430);
        root.setTop(menubar);
        root.setCenter(hBoxMain);
        return root;
    }

    private static void reRenderSeatingChart(HBox hBoxMain) {
        if (hBoxMain.getChildren().size() > 1) {
            hBoxMain.getChildren().remove(1);
        }
        hBoxMain.getChildren().add(1, renderSeatLayout(selectedPerformanceTicket));
    }

    private static VBox renderSeatLayout(PerformanceTicket performanceTicket) {
        SeatingChart seatingChart = performanceTicket.getSeatingChart();
        int rows =  seatingChart.getRows();
        int cols =  seatingChart.getColumns();
        GridPane fontSeatGrid = TheaterApplication.createSeatGrid(cols, 0, 5, seatingChart);
        fontSeatGrid.getStyleClass().add("fontSeatGrid");
        GridPane middleSeatGrid = TheaterApplication.createSeatGrid(cols, 5, 12, seatingChart);
        middleSeatGrid.getStyleClass().add("middleSeatGrid");
        GridPane backSeatGrid = TheaterApplication.createSeatGrid(cols, 12, rows, seatingChart);
        backSeatGrid.getStyleClass().add("backSeatGrid");

        VBox vbox1 = new VBox();
        vbox1.setSpacing(5);
        vbox1.setAlignment(Pos.CENTER);
        Performance performance = performanceTicket.getPerformance();
        Text text = new Text(String.format("%s - %s (%s - %s)",
                performance.getName(),
                performance.getStartTime().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)),
                performance.getStartTime().format(DateTimeFormatter.ofPattern("hh:mm a")),
                performance.getEndTime().format(DateTimeFormatter.ofPattern("hh:mm a"))));
        text.setTextAlignment(TextAlignment.LEFT);
        text.getStyleClass().add("title");
        Button screenBtn = new Button("SCREEN");
        screenBtn.getStyleClass().add("screen-btn");
        screenBtn.setPrefSize(300,50);
        vbox1.getChildren().addAll(text, screenBtn, fontSeatGrid, middleSeatGrid, backSeatGrid);

        return vbox1;
    }

    private static GridPane createSeatGrid(int cols, int fromRow, int toRow, SeatingChart seatingChart) {
        GridPane gridPane = new GridPane();
        gridPane.autosize();
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setPadding(new Insets(5, 5, 5, 5));

        for (int row=fromRow; row < toRow; row++) {
            Label rowLabel = new Label(String.valueOf(row + 1));
            gridPane.add(rowLabel, 0, row + 1);

            for (int column=0; column<cols ; column++) {
                if (row == 0) {
                    Label colLabel = new Label(String.valueOf(column + 1));
                    colLabel.setPrefWidth(40);
                    colLabel.setAlignment(Pos.CENTER);
                    gridPane.add(colLabel, column + 1, 0);
                }

                Seat seat = seatingChart.getSeat(column, row);
                boolean isReserved = seat.isReserved();

                Button button = new Button();
                button.setPrefSize(40,40);
                button.setId(String.format("%s-%s", row, column));
                button.getStyleClass().add("seatBtn");

                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if (enableUnreserved) {
                            if (isReserved) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setHeaderText(String.format(
                                        "Do you want to unreserved the Seat (%s-%s)?",
                                        seat.getRow() + 1,
                                        seat.getColumn() +1));
                                alert.getDialogPane().setPrefWidth(400);

                                ButtonType alertBtn = alert.showAndWait().orElse(ButtonType.CANCEL);
                                if(alertBtn == ButtonType.OK) {
                                    selectedPerformanceTicket.unreserveSeat(seat.getColumn(), seat.getRow());
                                    button.getStyleClass().remove("reservedSeatBtn");
                                } else {
                                    alert.close();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setHeaderText("Please select the reserved seat!!!");
                                alert.getDialogPane().setPrefWidth(400);
                                alert.show();
                            }
                        } else if (!isReserved) {
                            List<String> buttonClass = button.getStyleClass();
                            if (buttonClass.contains("choosingSeatBtn")) {
                                buttonClass.remove("choosingSeatBtn");
                                selectedSeats.remove(seat);
                            } else {
                                buttonClass.add("choosingSeatBtn");
                                selectedSeats.add(seat);
                            }
                        }
                    }
                });

                if (isReserved) {
                    button.getStyleClass().add("reservedSeatBtn");
                }
                button.getStyleClass().remove("choosingSeatBtn");
                gridPane.setMargin(button, new Insets(5, 5, 5, 5));
                gridPane.add(button, column + 1, row + 1);
            }
        }
        return gridPane;
    }

    private static GridPane createSeatNotes() {
        GridPane gridPane = TheaterApplication.createGridPanel();
        gridPane.getStyleClass().add("noteGrid");

        Button freeSeat = new Button();
        freeSeat.setPrefSize(30, 30);
        freeSeat.getStyleClass().add("freeSeatBtn");
        Label freeSeatLbl = new Label("Free seat");
        freeSeatLbl.getStyleClass().add("labelStyle");

        Button reservedSeat = new Button();
        reservedSeat.setPrefSize(30, 30);
        reservedSeat.getStyleClass().add("reservedSeatBtn");
        Label reservedSeatLbl = new Label("Reserved seat");
        reservedSeatLbl.getStyleClass().add("labelStyle");

        Button choosingSeat = new Button();
        choosingSeat.setPrefSize(30, 30);
        choosingSeat.getStyleClass().add("choosingSeatBtn");
        Label choosingSeatLbl = new Label("Choosing seat");
        choosingSeatLbl.getStyleClass().add("labelStyle");

        Button frontSeatBtn = new Button();
        frontSeatBtn.setPrefSize(30, 30);
        frontSeatBtn.getStyleClass().add("frontSeatBtn");
        Label frontSeatLbl = new Label(String.format("Front seat (%s)", Utils.getPrice(Section.FRONT.getPrice())));
        frontSeatLbl.getStyleClass().add("labelStyle");

        Button middleSeatBtn = new Button();
        middleSeatBtn.setPrefSize(30, 30);
        middleSeatBtn.getStyleClass().add("middleSeatBtn");
        Label middleSeatLbl = new Label(String.format("Middle seat (%s)", Utils.getPrice(Section.MIDDLE.getPrice())));
        middleSeatLbl.getStyleClass().add("labelStyle");

        Button backSeatBtn = new Button();
        backSeatBtn.setPrefSize(30, 30);
        backSeatBtn.getStyleClass().add("backSeatBtn");
        Label backSeatlbl = new Label(String.format("Back seat (%s)", Utils.getPrice(Section.BACK.getPrice())));
        backSeatlbl.getStyleClass().add("labelStyle");

        gridPane.add(freeSeat, 0, 0);
        gridPane.add(reservedSeat, 0, 1);
        gridPane.add(choosingSeat, 0, 2);
        gridPane.add(freeSeatLbl, 1, 0);
        gridPane.add(reservedSeatLbl, 1, 1);
        gridPane.add(choosingSeatLbl, 1, 2);

        gridPane.add(frontSeatBtn, 0, 3);
        gridPane.add(frontSeatLbl, 1, 3);
        gridPane.add(middleSeatBtn, 0, 4);
        gridPane.add(middleSeatLbl, 1, 4);
        gridPane.add(backSeatBtn, 0, 5);
        gridPane.add(backSeatlbl, 1, 5);

        return gridPane;
    }

    private static GridPane createGridPanel() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);  // between columns
        gridPane.setVgap(10);  //  between rows
        //top, right, bottom, left
        gridPane.setPadding(new Insets(15, 25, 15, 25));
        // To debug
//        gridPane.setGridLinesVisible(true);
        return gridPane;
    }

    private static TableColumn getTableColumn(String name) {
        TableColumn col = new TableColumn(name);
        col.setMinWidth(100);
        col.setStyle("-fx-alignment: CENTER;");
        return col;
    }

    public static class PerformanceProperty {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty name;

        private final SimpleStringProperty startTime;
        private final SimpleStringProperty endTime;

        public PerformanceProperty(int id, String name, String startTime, String endTime) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.startTime = new SimpleStringProperty(startTime);
            this.endTime = new SimpleStringProperty(endTime);
        }

        public int getId() {
            return id.get();
        }

        public SimpleIntegerProperty idProperty() {
            return id;
        }

        public void setId(int id) {
            this.id.set(id);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getStartTime() {
            return startTime.get();
        }

        public SimpleStringProperty startTimeProperty() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime.set(startTime);
        }

        public String getEndTime() {
            return endTime.get();
        }

        public SimpleStringProperty endTimeProperty() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime.set(endTime);
        }

        public static PerformanceProperty convertToProperty(Performance p) {
            return new PerformanceProperty(
                    p.getId(),
                    p.getName(),
                    Utils.fomatDateTime(p.getStartTime()),
                    Utils.fomatDateTime(p.getEndTime()));
        }
    }

}