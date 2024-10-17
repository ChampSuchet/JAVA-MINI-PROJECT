class Manager{
    public void addProductManager(product_class product,Inventory inventory){
        inventory.addProduct(product);
        System.out.println("Manager added:"+product.name+"to inventory");
    }
    public void showstats(Inventory inventory){
        inventory.showstatistics();
    }
}
class Inventorymain{
    public static void main(String[] args){
        Inventory inventory = new Inventory(10);

        Manager manager =  new Manager();

        manager.addProductManager(new goods(1, "laptop", 1000, 10), inventory);
        manager.addProductManager(new cargo(2, "Fridge", 800, 8), inventory);

        Customer customer = new Customer(5);

        customer.addProduct(inventory.getProduct(1));
        customer.addProduct(inventory.getProduct(2));

        customer.checkout();

        inventory.procureproduct(1, 4);
        inventory.procureproduct(2, 7);

        manager.showstats(inventory);
    }
}