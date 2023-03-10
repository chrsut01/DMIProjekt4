package com.example.dmiprojekt4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DMIProjektController {

        @FXML
        private ComboBox<Station> stationCB;

        @FXML
        private ComboBox<String> weatherDataCB;
        @FXML
        private ComboBox<String> startDateCB;
        @FXML
        private ComboBox<String> startTimeCB;

        @FXML
        private ComboBox<String> endDateCB;

        @FXML
        private ComboBox<String> endTimeCB;


        @FXML
        private AreaChart<String, Number> areaChartDays;
        @FXML
        private AreaChart<String, Number> areaChartHours;


        public void initialize() {

                areaChartDays.setVisible(false);
                areaChartHours.setVisible(false);

                Station station1 = new Station("06041", "Skagen Fyr", "’54.8978/9.1282", "3.2", "1 Jan 53");
                Station station2 = new Station("06068", "Isenvad", "’56.0939/9.1811", "\"60,35\"", "10 okt 2022");
                Station station3 = new Station("06104", "Billund Lufthavn", "’57.7354/10.6316", "75", "11 May 1973");
                Station station4 = new Station("06116", "Store Jyndevad", "’54.8978/9.1282", "\"15,3\"", "5 sep 1984");
                Station station5 = new Station("06159", "Røsnæs Fyr", "55.7433/10.8694", "14.16", "1 Jan 59");
                Station station6 = new Station("06193", "Hammer Odde Fyr", "55.2979/14.7718", "7.81", "1 Jan 59");

                stationCB.getItems().add(station1);
                stationCB.getItems().add(station2);
                stationCB.getItems().add(station3);
                stationCB.getItems().add(station4);
                stationCB.getItems().add(station5);
                stationCB.getItems().add(station6);


                weatherDataCB.getItems().addAll("Precip", "AvgTemp", "MaxTemp", "MinTemp", "Sunshine", "AvgWind",
                        "MaxWind", "CloudHeight", "CloudCover");

                startDateCB.getItems().addAll("2023-01-01", "2023-01-02", "2023-01-03", "2023-01-04", "2023-01-05",
                        "2023-01-06", "2023-01-07", "2023-01-08", "2023-01-09", "2023-01-10", "2023-01-11", "2023-01-12",
                        "2023-01-13", "2023-01-14", "2023-01-15", "2023-01-16", "2023-01-17", "2023-01-18", "2023-01-19",
                        "2023-01-20", "2023-01-21", "2023-01-22", "2023-01-23", "2023-01-24", "2023-01-25", "2023-01-26",
                        "2023-01-27", "2023-01-28", "2023-01-29", "2023-01-30", "2023-01-31");
                endDateCB.getItems().addAll("2023-01-01", "2023-01-02", "2023-01-03", "2023-01-04", "2023-01-05",
                        "2023-01-06", "2023-01-07", "2023-01-08", "2023-01-09", "2023-01-10", "2023-01-11", "2023-01-12",
                        "2023-01-13", "2023-01-14", "2023-01-15", "2023-01-16", "2023-01-17", "2023-01-18", "2023-01-19",
                        "2023-01-20", "2023-01-21", "2023-01-22", "2023-01-23", "2023-01-24", "2023-01-25", "2023-01-26",
                        "2023-01-27", "2023-01-28", "2023-01-29", "2023-01-30", "2023-01-31");

                startTimeCB.getItems().addAll("00:00:00", "01:00:00", "02:00:00", "03:00:00", "04:00:00", "05:00:00",
                        "06:00:00", "07:00:00", "08:00:00", "09:00:00", "10:00:00", "11:00:00", "12:00:00", "13:00:00",
                        "14:00:00", "15:00:00", "16:00:00", "17:00:00", "18:00:00", "19:00:00", "20:00:00", "21:00:00",
                        "22:00:00", "23:00:00");
                endTimeCB.getItems().addAll("00:00:00", "01:00:00", "02:00:00", "03:00:00", "04:00:00", "05:00:00",
                        "06:00:00", "07:00:00", "08:00:00", "09:00:00", "10:00:00", "11:00:00", "12:00:00", "13:00:00",
                        "14:00:00", "15:00:00", "16:00:00", "17:00:00", "18:00:00", "19:00:00", "20:00:00", "21:00:00",
                        "22:00:00", "23:00:00");
        }

        // Sets up the Daily Area Chart with the Show Daily Data button
        @FXML
        void showDailyData(ActionEvent event) {

                String insertStation = stationCB.getSelectionModel().getSelectedItem().getStationID();
                String insertStartDate = startDateCB.getSelectionModel().getSelectedItem();
                String insertStartTime = startTimeCB.getSelectionModel().getSelectedItem();
                String insertEndDate = endDateCB.getSelectionModel().getSelectedItem();
                String insertWeatherData = weatherDataCB.getSelectionModel().getSelectedItem();

                insertStartDate = "'" + insertStartDate + "'";
                insertStartTime = "'" + insertStartTime + "'";
                insertEndDate = "'" + insertEndDate + "'";

                int numberRows = 0;
                areaChartDays.setVisible(true);
                areaChartHours.setVisible(false);
                areaChartDays.getData().clear();

                Axis<String> xAxis = areaChartDays.getXAxis();
                xAxis.setLabel("Days");
                Axis<Number> yAxis = areaChartDays.getYAxis();
                yAxis.setLabel("Values");

                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(insertWeatherData);

                // Connects to DB and selects data for Daily Data Area Chart
                try (Connection conn = DBConnection.getConnection()) {
                        String sql = "SELECT " + insertWeatherData + ", Dato, Tid from WeatherData WHERE Dato >= " +
                                insertStartDate + " AND Dato <= " + insertEndDate + "AND Tid = " + insertStartTime +
                                " AND StationID = " + insertStation;

                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        System.out.println(sql);

                        while (rs.next()) {

                                ++numberRows;
                                String columnValue = rs.getString(insertWeatherData);
                                String dato = rs.getString("Dato");
                                System.out.println(columnValue + " " + dato);
                                if (columnValue.length() != 0)
                                   series.getData().add(new XYChart.Data(dato + " ",  Double.parseDouble(columnValue)));

                        }

                        areaChartDays.getData().add(series);
                        System.out.println(numberRows);
                }
                        catch(
                                SQLException sqlse){
                                System.out.println(sqlse);
                        }
                        System.out.println("");
        }
        // Sets up the Daily Area Chart with the Show Hourly Data button
        @FXML
        void showHourlyData(ActionEvent event) {

                String insertStation = stationCB.getSelectionModel().getSelectedItem().getStationID();
                String insertStartDate = startDateCB.getSelectionModel().getSelectedItem();
                String insertStartTime = startTimeCB.getSelectionModel().getSelectedItem();
                String insertEndTime = endTimeCB.getSelectionModel().getSelectedItem();
                String insertWeatherData = weatherDataCB.getSelectionModel().getSelectedItem();

                insertStartDate = "'" + insertStartDate + "'";
                insertStartTime = "'" + insertStartTime + "'";
                insertEndTime = "'" + insertEndTime + "'";

                int numberRows = 0;
                areaChartHours.setVisible(true);
                areaChartDays.setVisible(false);
                areaChartHours.getData().clear();

                Axis<String> xAxis = areaChartHours.getXAxis();
                xAxis.setLabel("Hours");
                Axis<Number> yAxis = areaChartHours.getYAxis();
                yAxis.setLabel("Values");

                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(insertWeatherData);

                // Connects to DB and selects data for Hourly Data Area Chart
                try (Connection conn = DBConnection.getConnection()) {

                        String sql = "SELECT " + insertWeatherData + ", Dato, Tid from WeatherData WHERE Dato = " +
                                insertStartDate + " AND Tid >= " + insertStartTime + " AND Tid <= " + insertEndTime +
                                " AND StationID = " + insertStation;

                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        System.out.println(sql);

                        while (rs.next()) {

                                ++numberRows;
                                String columnValue = rs.getString(insertWeatherData);
                                String dato = rs.getString("Dato");
                                String tid = rs.getString("Tid");
                                System.out.println(columnValue + " " + dato);
                                series.getData().add(new XYChart.Data(" " + tid, Double.parseDouble(columnValue)));

                        }
                        areaChartHours.getData().add(series);
                        System.out.println(numberRows);
                }
                catch(
                        SQLException sqlse){
                        System.out.println(sqlse);
                }
                System.out.println("");
                }
}

