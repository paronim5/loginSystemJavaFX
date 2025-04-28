module org.example.loginsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires spring.security.crypto;    // java: cannot access javax.naming.Referenceable class file for javax.naming.Referenceable not found

    opens org.example.loginsystem to javafx.fxml, org.hibernate.orm.core;
    exports org.example.loginsystem;
}