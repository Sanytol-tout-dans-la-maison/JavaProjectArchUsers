module org.isep.javaprojectarchusers {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires java.logging;
    requires javafx.graphics;
    requires java.desktop;
    requires org.jfree.jfreechart;
    requires org.jfree.chart.fx;

    opens org.isep.javaprojectarchusers to javafx.fxml;
    exports org.isep.javaprojectarchusers;
    exports org.isep.javaprojectarchusers.Accounts;
    opens org.isep.javaprojectarchusers.Accounts to javafx.fxml;
    exports org.isep.javaprojectarchusers.GUI;
    opens org.isep.javaprojectarchusers.GUI to javafx.fxml;
    exports org.isep.javaprojectarchusers.Encryption;
    opens org.isep.javaprojectarchusers.Encryption to javafx.fxml;
    exports org.isep.javaprojectarchusers.Blockchain;
    opens org.isep.javaprojectarchusers.Blockchain to javafx.fxml;
    exports org.isep.javaprojectarchusers.Assets;
    opens org.isep.javaprojectarchusers.Assets to javafx.fxml;
    exports org.isep.javaprojectarchusers.Events;
    opens org.isep.javaprojectarchusers.Events to javafx.fxml;
    exports org.isep.javaprojectarchusers.ReadWriteFile;
    opens org.isep.javaprojectarchusers.ReadWriteFile to javafx.fxml;
}