package com.example.demo1;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.HashMap;


public class Mini extends Application {
    private Inventory inventory;
    private Customer customer;
    private Manager manager;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventory Management System");

        inventory = new Inventory();

        manager = new Manager();

        TabPane tabPane = new TabPane();
        Tab managerTab = new Tab("Manager");
        Tab customerTab = new Tab("Customer");
        customerTab.setClosable(false);
        managerTab.setClosable(false);

        GridPane managerGrid = new GridPane();
        managerTab.setContent(managerGrid);

        Label idLabel = new Label("Product ID:");
        TextField idInput = new TextField();
        managerGrid.add(idLabel, 0, 1);
        managerGrid.add(idInput, 1, 1);

        Label nameLabel = new Label("Product Name:");
        TextField nameInput = new TextField();
        managerGrid.add(nameLabel, 0, 2);
        managerGrid.add(nameInput, 1, 2);

        Label quantityLabel = new Label("Quantity:");
        TextField quantityInput = new TextField();
        managerGrid.add(quantityLabel, 0, 3);
        managerGrid.add(quantityInput, 1, 3);

        Label priceLabel = new Label("Price:");
        TextField priceInput = new TextField();
        managerGrid.add(priceLabel, 0, 4);
        managerGrid.add(priceInput, 1, 4);

        Label modeLabel = new Label("Mode (Land/Sea):");
        TextField modeInput = new TextField();
        managerGrid.add(modeLabel, 0, 5);
        managerGrid.add(modeInput, 1, 5);

        Button addButton = new Button("Add Product");
        managerGrid.add(addButton, 1, 6);

        addButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idInput.getText());
                String name = nameInput.getText();
                int quantity = Integer.parseInt(quantityInput.getText());
                double price = Double.parseDouble(priceInput.getText());
                String mode = modeInput.getText();

                Product product = new Product(id, name, price, quantity, mode);
                manager.addProduct(product, inventory);

                idInput.clear();
                nameInput.clear();
                quantityInput.clear();
                priceInput.clear();
                modeInput.clear();
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input. Please enter valid numbers.");
            }
        });

        VBox customerLayout = new VBox();
        customerTab.setContent(customerLayout);

        TableView<Product> productTable = new TableView<>();
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Product, String> modeCol = new TableColumn<>("Mode");
        modeCol.setCellValueFactory(new PropertyValueFactory<>("mode"));



        productTable.getColumns().add(nameCol);
        productTable.getColumns().add(quantityCol);
        productTable.getColumns().add(priceCol);
        productTable.getColumns().add(modeCol);


        productTable.setItems(inventory.getProductList());
        customerLayout.getChildren().add(productTable);

        Label orderLabel = new Label("Order Product:");
        TextField orderIdInput = new TextField();
        orderIdInput.setPromptText("Enter ID");
        TextField orderQuantityInput = new TextField();
        orderQuantityInput.setPromptText("Enter quantity");
        Button orderButton = new Button("Order Product");

        customerLayout.getChildren().addAll(orderLabel, orderIdInput, orderQuantityInput, orderButton);

        orderButton.setOnAction(e -> {
            try {
                int productId = Integer.parseInt(orderIdInput.getText());
                int quantity = Integer.parseInt(orderQuantityInput.getText());

                if (inventory.procureProduct(productId, quantity)) {
                    Product product = inventory.getProduct(productId);
                    if (product != null) {
                        customer = new Customer();
                        customer.addProduct(product);
                        System.out.println("Total price: $" + customer.calculateTotal(quantity,product));
                    }
                }

                orderIdInput.clear();
                orderQuantityInput.clear();
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input. Please enter valid numbers.");
            }
        });

        tabPane.getTabs().addAll(managerTab, customerTab);
        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}




package com.example.demo1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

class Inventory {
    private final ObservableList<Product> products;

    public Inventory() {
        products = FXCollections.observableArrayList();
    }

    void addProduct(Product product) {
        products.add(product);
    }

    Product getProduct(int productId) {
        for (Product product : products) {
            if (product.idProperty().get() == productId) {
                return product;
            }
        }
        return null;
    }

    ObservableList<Product> getProductList() {
        return products;
    }

    boolean procureProduct(int productId, int quantity) {
        Product product = getProduct(productId);
        if (product != null && !product.isOutOfStock() && product.quantityProperty().get() >= quantity) {
            product.reduceQuantity(quantity);
            if (product.quantityProperty().get() < 5) {
                System.out.println("Low stock alert for " + product.nameProperty().get());
            }
            return true;
        }
        System.out.println("Product not found or insufficient stock.");
        return false;
    }

}



package com.example.demo1;

import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

class Customer {
    private final ObservableList<Product> cart;

    Customer() {
        cart = FXCollections.observableArrayList();
    }

    void addProduct(Product product) {
        cart.add(product);
    }

    double calculateTotal(int quantity, Product product)  {
        Double price=0.0;
        for(int i =0; i<cart.size();i++){
            if(cart.get(i).getId()==product.getId()){
                price =Double.valueOf(quantity)*cart.get(i).priceProperty().doubleValue();
                break;
            }
        }
return price;
    }

    ObservableList<Product> getCart() {
        return cart;
    }
}



package com.example.demo1;

import javafx.beans.property.*;

public class Product {
    private final IntegerProperty id;
    private final StringProperty name;
    private final IntegerProperty quantity;
    private final DoubleProperty price;
    private final StringProperty mode;

    Product(int id, String name, double price, int quantity, String mode) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.mode = new SimpleStringProperty(mode);
    }

    public boolean isOutOfStock() {
        return this.quantity.get() <= 0;
    }

    public void reduceQuantity(int x) {
        if (quantity.get() >= x) {
            this.quantity.set(this.quantity.get() - x);
        } else {
            System.out.println("Insufficient stock for " + name.get());
        }
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public StringProperty modeProperty() {
        return mode;
    }

    public int getId() {
        return id.get();
    }
}



package com.example.demo1;

public class Manager {
    public void addProduct(Product product, Inventory inventory) {
        inventory.addProduct(product);
    }
}