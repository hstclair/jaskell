import com.hstclair.jaskell.fifth.demo.FifthDebug;
import com.hstclair.jaskell.fifth.demo.FifthFibDemo;
import com.hstclair.jaskell.fifth.demo.FifthIterationDemo;
import com.hstclair.jaskell.fifth.function.Expression;
import com.hstclair.jaskell.fourth.demo.FourthDemo;

import java.math.BigInteger;
import java.util.function.Supplier;

public class Main {

    static long enanos(long lastnanos) {
        return System.nanoTime() - lastnanos;
    }

    public static void fifthFibDemo(long n) {
        long lastNanos = enanos(0);

        FifthFibDemo fifthFibDemo = new FifthFibDemo();

        System.out.printf("%s Preparing\n", lastNanos = enanos(lastNanos));
        Expression<BigInteger> fifthResultSupplier = fifthFibDemo.fib(n);

        System.out.printf("%s Evaluating\n", lastNanos = enanos(lastNanos));
        BigInteger fifthResult = fifthResultSupplier.evaluate();

        System.out.printf("%s Displaying\n", lastNanos = enanos(lastNanos));
        System.out.printf("Result from FifthFibDemo is %s\n", fifthResult);
    }

    public static void fourthFibDemo(long n) {
        long lastNanos = enanos(0);

        FourthDemo fourthDemo = new FourthDemo();

        System.out.printf("%s Preparing\n", lastNanos = enanos(lastNanos));
        Supplier<BigInteger> fourthResultSupplier = fourthDemo.fib(n);

        System.out.printf("%s Evaluating\n", lastNanos = enanos(lastNanos));
        BigInteger fourthResult = fourthResultSupplier.get();

        System.out.printf("%s Displaying\n", lastNanos = enanos(lastNanos));
        System.out.printf("Result from FourthDemo is %s\n", fourthResult);
    }


    public static void main(String[] args) {
        long n = 1000000;

//        fifthFibDemo(n);
//        fourthFibDemo(n);


        FifthIterationDemo.demo();

//        FifthDebug.debug();

//        String something = "some text";
//
//        Character[] array = buildCharArray(something);
//
//        LzList<Character> lzList = new LzList<Character>(array);
//
//
//        Supplier<Character> result = lzList.map(Character::toUpperCase).take(4).last();
//
//        System.out.println(result.get());
    }
}
