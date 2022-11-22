module com.oh72.computergraphics {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
//    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
    requires javafx.swing;
    requires java.desktop;

    opens com.oh72.computergraphics to javafx.fxml;
    exports com.oh72.computergraphics;
}