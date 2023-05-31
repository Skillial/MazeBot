module csintsy.mco1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens csintsy.mco1 to javafx.fxml;
    exports csintsy.mco1;
}