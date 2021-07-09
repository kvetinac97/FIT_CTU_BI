package kvetinac97;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField fName;
    @FXML
    private TextField fPrice;

    @FXML
    private TableView<Product> fTable;

    @FXML
    private TableColumn<Product, String> fColId;

    @FXML
    private TableColumn<Product, String> fColName;

    @FXML
    private TableColumn<Product, Integer> fColPrice;

    @FXML
    private TableColumn<Product, Integer> fColCount;

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        // Here, we would, initialize it
        fColId.setCellValueFactory( product -> new ReadOnlyObjectWrapper<>(product.getValue().id) );
        fColName.setCellValueFactory( product -> new ReadOnlyObjectWrapper<>(product.getValue().name) );
        fColPrice.setCellValueFactory( product -> new ReadOnlyObjectWrapper<>(product.getValue().price) );
        fColCount.setCellValueFactory( product -> new ReadOnlyObjectWrapper<>(product.getValue().count) );
    }

    @FXML
    public void addItem () {
        fTable.getItems().add(new Product(Integer.toString(new Random().nextInt(50)),
                fName.getText(), Integer.parseInt(fPrice.getText()), 0));
        fName.clear();
        fPrice.clear();
    }

    @FXML
    public void sellItem () {
        fTable.getItems().stream()
                .filter( product -> product.equals( fTable.getSelectionModel().getSelectedItem() ) )
                .forEach( product -> product.count-- );
        fTable.refresh();
    }

    @FXML
    public void returnItem () {
        fTable.getItems().stream()
                .filter( product -> product.equals( fTable.getSelectionModel().getSelectedItem() ) )
                .forEach( product -> product.count++ );
        fTable.refresh();
    }

    static class Product {

        public String id;
        public String name;

        public Integer price;
        public Integer count;

        public Product ( String id, String name, Integer price, Integer count ) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.count = count;
        }

        @Override
        public boolean equals ( Object o ) {
            if ( this == o )
                return true;

            if ( o == null || getClass() != o.getClass() )
                return false;

            Product product = (Product) o;
            return name.equals(product.name);
        }

    }

}
