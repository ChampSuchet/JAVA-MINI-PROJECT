import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

class Product {
    int id;
    String name;
    int quantity;
    String mode;
    double price;

    Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getShipmentMode() {
        return mode;
    }

    boolean isOutOfStock() {
        return this.quantity <= 0;
    }

    void reduceQuantity(int x) {
        if (!isOutOfStock()) {
            this.quantity -= x;
        } else {
            System.out.println("Sorry, the product is out of stock");
        }
    }

    void displayProduct() {
        System.out.println("Product ID: " + this.id);
        System.out.println("Product Name: " + this.name);
        System.out.println("Mode of Transportation: " + this.mode);
    }
}

class Goods extends Product {
    Goods(int id, String name, double price, int quantity) {
        super(id, name, price, quantity);
        this.mode = "Land";
    }
}

class Cargo extends Product {
    Cargo(int id, String name, double price, int quantity) {
        super(id, name, price, quantity);
        this.mode = "Sea"; // Set shipment mode to "Sea"
    }
}

class Inventory {
    Product[] products;
    int productCount = 0;
    final int stockLimit = 5;

    public Inventory(int size) {
        products = new Product[size];
    }

    void addProduct(Product product) {
        if (productCount < products.length) {
            products[productCount++] = product;
        } else {
            System.out.println("Inventory is already full.");
        }
    }

    public Product getProduct(int productId) {
        for (int i = 0; i < productCount; i++) {
            if (products[i].id == productId) {
                return products[i];
            }
        }
        return null;
    }

    public void procureProduct(int productId, int q1) {
        Product product = getProduct(productId);
        if (product != null) {
            if (product.isOutOfStock()) {
                System.out.println("Product " + productId + " is out of stock.");
            } else {
                product.reduceQuantity(q1);
                System.out.println("Ordered " + q1 + " of " + product.name + ". Remaining stock: " + product.quantity);
                if (product.quantity < stockLimit) {
                    System.out.println("Low stock alert for " + product.name + ". Quantity: " + product.quantity);
                }
            }
        }
    }

    public void showStatistics() {
        int totalCargo = 0;
        int totalGoods = 0;

        for (int i = 0; i < productCount; i++) {
            if (products[i].mode.equals("Land")) {
                totalGoods++;
            } else {
                totalCargo++;
            }
        }
        System.out.println("Total Cargo: " + totalCargo + ", Total Goods: " + totalGoods);
    }
}

class Customer {
    Product[] cart;
    int itemCount = 0;

    Customer(int size) {
        cart = new Product[size];
    }

    void addProduct(Product prod) {
        if (itemCount < cart.length) {
            cart[itemCount++] = prod;
            System.out.println("Product added to cart: " + prod.name);
        } else {
            System.out.println("Cart is full. Cannot add more items.");
        }
    }

    void checkout() {
        double finalPrice = 0;

        for (int i = 0; i < itemCount; i++) {
            finalPrice += cart[i].quantity * cart[i].price;
        }

        System.out.println("Total price of the items in the cart: " + finalPrice);

        if (finalPrice == 0) {
            System.out.println("No items in cart.");
        } else {
            System.out.println("PURCHASE FINALIZED");
        }
    }
}

class Manager {
    public void addProductManager(Product product, Inventory inventory) {
        inventory.addProduct(product);
        System.out.println("Manager added: " + product.name + " to inventory");
    }

    public void showStats(Inventory inventory) {
        inventory.showStatistics();
    }
}

public class Mini extends Application {
    private Inventory inventory;
    private TextArea resultArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventory Management System");

        // Initialize inventory with a specified size
        inventory = new Inventory(10);

        // Create layout
        GridPane grid = new GridPane();
        Scene scene = new Scene(grid, 600, 400);

        // Add Product Section
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

        Label categoryLabel = new Label("Category (Goods/Cargo):");
        TextField categoryInput = new TextField();
        grid.add(categoryLabel, 0, 5);
        grid.add(categoryInput, 1, 5);

        Button addButton = new Button("Add Product");
        grid.add(addButton, 1, 6);

        // Order Product Section
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

        resultArea = new TextArea();
        resultArea.setEditable(false);
        grid.add(resultArea, 0, 11, 2, 1);

        // Button Actions
        addButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idInput.getText());
                String name = nameInput.getText();
                int quantity = Integer.parseInt(quantityInput.getText());
                double price = Double.parseDouble(priceInput.getText());
                String category = categoryInput.getText();

                Product product;
                if (category.equalsIgnoreCase("Goods")) {
                    product = new Goods(id, name, price, quantity);
                } else if (category.equalsIgnoreCase("Cargo")) {
                    product = new Cargo(id, name, price, quantity);
                } else {
                    resultArea.appendText("Invalid category. Use 'Goods' or 'Cargo'.\n");
                    return;
                }

                Manager manager = new Manager();
                manager.addProductManager(product, inventory);
                resultArea.appendText("Product added successfully: " + name + "\n");

                // Clear inputs
                idInput.clear();
                nameInput.clear();
                quantityInput.clear();
                priceInput.clear();
                categoryInput.clear();
            } catch (NumberFormatException ex) {
                resultArea.appendText("Invalid input. Please enter numbers for ID, quantity, and price.\n");
            }
        });

        orderButton.setOnAction(e -> {
            try {
                int productId = Integer.parseInt(orderIdInput.getText());
                int quantity = Integer.parseInt(orderQuantityInput.getText());
                Customer customer = new Customer(5);
                Product product = inventory.getProduct(productId);
                if (product != null) {
                    customer.addProduct(product);
                    customer.checkout();
                    inventory.procureProduct(productId, quantity);
                } else {
                    resultArea.appendText("Product not found.\n");
                }

                // Clear inputs
                orderIdInput.clear();
                orderQuantityInput.clear();
            } catch (NumberFormatException ex) {
                resultArea.appendText("Invalid input. Please enter numbers for Product ID and quantity.\n");
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
