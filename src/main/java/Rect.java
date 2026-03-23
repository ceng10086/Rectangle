import java.util.Comparator;

public class Rect {

    private int length;
    private int width;

    public Rect(int length, int width) {
        if (length < 0 || width < 0) {
            throw new IllegalArgumentException("Length and width must be non-negative");
        }
        this.length = length;
        this.width = width;
    }

    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length must be non-negative");
        }
        this.length = length;
    }

    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        if (width < 0) {
            throw new IllegalArgumentException("Width must be non-negative");
        }
        this.width = width;
    }

    public int getArea() {
        return length*width;
    }
    public int getPerimeter() {
        return length*2 + width*2;
    }

    public String getObject() {
        return "("+length+","+width+")";
    }
    public static <AnyType>
    AnyType findMax(AnyType[] arr, Comparator<? super AnyType> cmp) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }
        if (cmp == null) {
            throw new IllegalArgumentException("Comparator must not be null");
        }

        int maxIndex = 0;

        for(int i = 1; i < arr.length; i++)
            if(cmp.compare(arr[i], arr[maxIndex]) > 0)
                maxIndex = i;

        return arr[maxIndex];
    }

    public static class areaCompare implements Comparator<Rect> {
        @Override
        public int compare(Rect o1, Rect o2) {
            if(o1.getArea() > o2.getArea()) {
                return 1;
            }else if(o1.getArea() == o2.getArea()) {
                return 0;
            }else{
                return -1;
            }
        }
    }

    public static class perimeterCompare implements Comparator<Rect> {
        @Override
        public int compare(Rect o1, Rect o2) {
            if(o1.getPerimeter() > o2.getPerimeter()) {
                return 1;
            }else if(o1.getPerimeter() == o2.getPerimeter()) {
                return 0;
            }else{
                return -1;
            }
        }
    }

    public static void main(String[] args) {
        Rect[] arr = new Rect[] {
                new Rect(10, 20), new Rect(2, 65),
                new Rect(3, 10), new Rect(6, 20)
                };

        System.out.println("Max Area: "+findMax(arr, new areaCompare()).getObject());
        System.out.println("Max Perimeter: "+findMax(arr, new perimeterCompare()).getObject());
    }
}
