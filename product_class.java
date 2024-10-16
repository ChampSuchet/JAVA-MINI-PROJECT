public class product_class {
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