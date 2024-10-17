class product_class {
    int id;
    String name;
    int quantity;
    String mode;
    double price;
   
   product_class(int id,String name,double price,int quantity){
       this.id=id;
       this.name=name;
       this.price=price;
       this.quantity=quantity;
        
   }
    public String getShipmentMode() {
           return mode;
       }
    
   boolean isoutofstock() {
       if(this.quantity<0) {
           return false;
           
       }
       else {
           return true;
       }
   }
   //
   void reducequantity(int x) {
       if(!isoutofstock()) {
           this.quantity=this.quantity-x;
       }
       else {
           System.out.println("Sorry, the product is out of stock");
       }
   }
   void displayproduct() {
       System.out.println("product id is"+this.id);
       System.out.println("product name is"+this.name);
       System.out.println("mode of transportation"+this.mode);
   }
   



}
class goods extends product_class{
   goods(int id,String name,double price,int quantity){
       super(id,name,price,quantity);
       this.mode="land";
        
   }
   
}
class cargo extends product_class {
   // Constructor
     cargo(int productId, String name, double price, int quantity) {
       super(productId, name, price, quantity);
       this.mode = "Sea"; // Set shipment mode to "Sea"
   }
}

class Inventory{
    product_class[] products;
    int productcount=0;
    final int Stock_limit = 5;

    public Inventory(int size){
        products = new product_class[size];
    }
    void addProduct(product_class product){
        if(productcount<products.length){
            products[productcount++] = product;
        }else{
            System.out.println("already full");
        }
    }
    public product_class getProduct(int productid){
        for(int i=0;i<productcount;i++){
            if(products[i].id == productid){
                return products[i];
            }
        }
        return null;
    }
    public void procureproduct(int productid,int q1){
        product_class product = getProduct(productid);
        if(product!=null){
            if(product.isoutofstock()){
                System.out.println("Product "+productid+"is out of stock");
            }else{
                product.reducequantity(q1);
                System.out.println("Product "+q1+"of"+product.name+".Remaining stock"+product.quantity);
                if(product.quantity<Stock_limit){
                    System.out.println("low stock alert"+product.name+"Quanity"+product.quantity);
                }
            }
        }
    }
    public void showstatistics(){
        int totalcargo=0;
        int totalgood=0;

        for(int i=0;i<productcount;i++){
            if(products[i].mode=="LAND"){
                totalgood++;
            }else{
                totalcargo++;
            }
        }
        System.out.println("cargo:"+totalcargo+"goods:"+totalgood);
    }
}
class Customer {
    
    product_class[] cart;
    int itemCount = 0;

    Customer(int size){
        cart = new product_class[size];
    }

    void addProduct(product_class prod){

        int ind = 0;

        if(ind==cart.length){

            System.out.println("Cart is full. Cannot add items");

        }

        else{

            cart[ind] = prod;
            System.out.println("Product added to cart : " + cart[ind].name);
            ind++;

        }

        return;

    }

    void checkout(){

        double fprice=0;

        for(int i = 0 ; i<cart.length ; i++){
            fprice += cart[i].quantity * cart[i].price;
        }

        System.out.println("Total price of the items in the cart : " + fprice);
        
        if(fprice == 0){

            System.out.println("No items in cart.");
            return;

        }

        else{

            System.out.println("PURCHASE FINALIZED");
            
        }

    }

}
class Manager{
    public void addProductManager(product_class product,Inventory inventory){
        inventory.addProduct(product);
        System.out.println("Manager added:"+product.name+"to inventory");
    }
    public void showstats(Inventory inventory){
        inventory.showstatistics();
    }
}
public class Mini{
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