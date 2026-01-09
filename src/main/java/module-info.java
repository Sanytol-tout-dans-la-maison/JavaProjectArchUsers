module org.isep.javaprojectarchusers {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.isep.javaprojectarchusers to javafx.fxml;
    exports org.isep.javaprojectarchusers;
}