module org.example.iisscommentbuilder {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens org.example.iisscommentbuilder to javafx.fxml;
    exports org.example.iisscommentbuilder;
}