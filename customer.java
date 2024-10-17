class Customer {
    
    product[] cart;
    int itemCount = 0;

    Customer(int size){
        cart = new product[size];
    }

    void addProduct(product prod){

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
