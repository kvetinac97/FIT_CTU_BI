import cz.kvetinac97.product.ProductNotebook;
import cz.kvetinac97.product.ProductNotebookCategory;
import cz.kvetinac97.product.ProductTelevision;
import cz.kvetinac97.product.computer.ComputerPart;
import cz.kvetinac97.product.computer.Memory;
import cz.kvetinac97.product.computer.Processor;
import cz.kvetinac97.product.computer.ProcessorSpeed;

public class MainClass {

    public final static Processor intel = new Processor(ProcessorSpeed.FAST);
    public final static Processor amd = new Processor(ProcessorSpeed.FAST);

    public final static Memory memory200GB = new Memory(200);
    public final static Memory memory500GB = new Memory(500);

    public final static ProductNotebook lenovoE500 = new ProductNotebook("Lenovo E500", 10000, 2, ProductNotebookCategory.BASIC, new ComputerPart[]{intel, memory200GB});
    public final static ProductNotebook hpBusinessPlus = new ProductNotebook("HP Business Plus", 40000, 5, ProductNotebookCategory.PROFESSIONAL, new ComputerPart[]{amd, memory500GB});

    public final static ProductTelevision samsungMediaPlus = new ProductTelevision("Samsung Media Plus", 5000, 2);

    public static void main (String[] args) {
        System.out.println("Appears to be working!");
        System.out.println(lenovoE500.toString());
    }

}
