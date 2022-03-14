package stage2.practice.one;

import java.util.BitSet;
import java.util.stream.IntStream;

public class Archiver {
    BitSet bitSet;
    Integer numBits;

    public BitSet compress(String input) {
        int length = input.length();
        this.numBits = length * 2;
        bitSet = new BitSet(length * 2);

        for (int i = 0, num = 0; i < length; i++, num += 2) {
            switch (input.charAt(i)) {
                case 'A'->{}
                case 'C' -> bitSet.set(num + 1, true);
                case 'G' -> bitSet.set(num, true);
                case 'T' -> bitSet.set(num, num + 2, true);
                default -> throw new IllegalStateException("Unexpected value: " + input.charAt(i));
            }
        }
        return bitSet;
    }
    public String decompress() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < numBits; i+=2) {
            if(!bitSet.get(i) && !bitSet.get(i + 1)) {
                res.append("A");
                continue;
            }
            if(!bitSet.get(i) && bitSet.get(i + 1)) {
                res.append("C");
                continue;
            }
            if(bitSet.get(i) && !bitSet.get(i + 1)) {
                res.append("G");
                continue;
            }
            if(bitSet.get(i) && bitSet.get(i + 1)) {
                res.append("T");
                continue;
            }
        }
        return res.toString();
    }

    public void printView() {
        StringBuilder view = new StringBuilder();
        IntStream.range(0, numBits)
                .mapToObj(i -> bitSet.get(i) ? '1' : '0')
                .forEach(view::append);
        System.out.println(view);
    }

    public static void main(String[] args) {
        Archiver archiver = new Archiver();
        archiver.compress("CAT");//упаковка
        archiver.printView();//побитовое отображение
        System.out.println(archiver.decompress());//распаковка
    }
}
