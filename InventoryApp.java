import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

class Product {
    private String id;
    private String name;
    private int quantity;
    private double price;
    private String category; // Good or Cargo

    public Product(String id, String name, int quantity, double price, String category) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public void reduceQuantity(int amount) {
        if (amount <= quantity) {
            quantity -= amount;
        }
    }

    public boolean isOutOfStock() {
        return quantity <= 0;
    }

    public boolean needsNotification() {
        return quantity < 5; // Example threshold for notification
    }
}

class Inventory {
    private Product[] products;
    private int productCount;

    public Inventory(int size) {
        products = new Product[size];
        productCount = 0;
    }

    public synchronized void addProduct(Product product) {
        if (productCount < products.length) {
            products[productCount++] = product;
        }
    }

    public synchronized String procureProduct(String productId, int quantity) {
        for (int i = 0; i < productCount; i++) {
            Product product = products[i];
            if (product.getId().equals(productId)) {
                if (product.isOutOfStock()) {
                    return "Product " + product.getName() + " is out of stock.";
                } else if (quantity <= product.getQuantity()) {
                    product.reduceQuantity(quantity);
                    if (product.needsNotification()) {
                        return "Notification: Low stock for " + product.getName();
                    }
                    return "Order placed for " + quantity + " of " + product.getName();
                } else {
                    return "Not enough stock for " + product.getName() + ".";
                }
            }
        }
        return "Product not found.";
    }

    public synchronized String[] getProductList() {
        String[] productList = new String[productCount];
        for (int i = 0; i < productCount; i++) {
            Product product = products[i];
            productList[i] = product.getId() + ": " + product.getName() + ", Quantity: " + product.getQuantity() + ", Price: " + product.getPrice() + ", Category: " + product.getCategory();
        }
        return productList;
    }
}

class User {
    protected String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

class Manager extends User {
    private Inventory inventory;

    public Manager(String username, Inventory inventory) {
        super(username);
        this.inventory = inventory;
    }

    public void addProduct(String id, String name, int quantity, double price, String category) {
        Product product = new Product(id, name, quantity, price, category);
        inventory.addProduct(product);
    }
}

class Customer extends User {
    private Inventory inventory;

    public Customer(String username, Inventory inventory) {
        super(username);
        this.inventory = inventory;
    }

    public String orderProduct(String productId, int quantity) {
        return inventory.procureProduct(productId, quantity);
    }
}

public class InventoryApp extends Application {
    private Inventory inventory;

    @Override
    public void start(Stage primaryStage) {
        inventory = new Inventory(10);

        // Create layout
        GridPane grid = new GridPane();
        Scene scene = new Scene(grid, 600, 400);

        // Add product section
        Label addProductLabel = new Label("Add Product:");
        grid.add(addProductLabel, 0, 0);

        Label idLabel = new Label("Product ID:");
        TextField idInput = new TextField();
        grid.add(idLabel, 0, 1);
        grid.add(idInput, 1, 1);

        Label nameLabel = new Label("Product Name:");
        TextField nameInput = new TextField();
        grid.add(nameLabel, 0, 2);
        grid.add(nameInput, 1, 2);

        Label quantityLabel = new Label("Quantity:");
        TextField quantityInput = new TextField();
        grid.add(quantityLabel, 0, 3);
        grid.add(quantityInput, 1, 3);

        Label priceLabel = new Label("Price:");
        TextField priceInput = new TextField();
        grid.add(priceLabel, 0, 4);
        grid.add(priceInput, 1, 4);

        Label categoryLabel = new Label("Category (Good/Cargo):");
        TextField categoryInput = new TextField();
        grid.add(categoryLabel, 0, 5);
        grid.add(categoryInput, 1, 5);

        Button addButton = new Button("Add Product");
        grid.add(addButton, 1, 6);

        // Order product section
        Label orderProductLabel = new Label("Order Product:");
        grid.add(orderProductLabel, 0, 7);

        Label orderIdLabel = new Label("Product ID:");
        TextField orderIdInput = new TextField();
        grid.add(orderIdLabel, 0, 8);
        grid.add(orderIdInput, 1, 8);

        Label orderQuantityLabel = new Label("Quantity:");
        TextField orderQuantityInput = new TextField();
        grid.add(orderQuantityLabel, 0, 9);
        grid.add(orderQuantityInput, 1, 9);

        Button orderButton = new Button("Order Product");
        grid.add(orderButton, 1, 10);

        Label resultLabel = new Label();
        grid.add(resultLabel, 0, 11, 2, 1);

        // Button actions
        addButton.setOnAction(e -> {
            String id = idInput.getText();
            String name = nameInput.getText();
            int quantity;
            double price;
            String category = categoryInput.getText();

            try {
                quantity = Integer.parseInt(quantityInput.getText());
                price = Double.parseDouble(priceInput.getText());
                Manager manager = new Manager("Manager", inventory);
                manager.addProduct(id, name, quantity, price, category);

                // Clear inputs
                idInput.clear();
                nameInput.clear();
                quantityInput.clear();
                priceInput.clear();
                categoryInput.clear();

                resultLabel.setText("Product added successfully.");
            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid quantity or price.");
            }
        });

        orderButton.setOnAction(e -> {
            String productId = orderIdInput.getText();
            int quantity;
            try {
                quantity = Integer.parseInt(orderQuantityInput.getText());
                Customer customer = new Customer("Customer", inventory);
                String result = customer.orderProduct(productId, quantity);
                resultLabel.setText(result);

                // Clear inputs
                orderIdInput.clear();
                orderQuantityInput.clear();
            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid quantity.");
            }
        });

        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
