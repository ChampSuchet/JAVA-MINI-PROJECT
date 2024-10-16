
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
