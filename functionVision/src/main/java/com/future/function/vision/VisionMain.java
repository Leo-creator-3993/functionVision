package com.future.function.vision;

import com.future.function.vision.tool.OtherTool;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class VisionMain {
    class X {
        public void m1() {
            System.out.println("X.m1()");
        }

        public void x() {
            System.out.println("x.x()");
        }
    }

    class A extends X {
        @Override
        public void m1() {
            System.out.println("A.m1()");
        }

        public void a() {
            System.out.println("a.a()");
        }
    }

    class B extends A {
        @Override
        public void m1() {
            System.out.println("B.m1()");
        }

        public void b() {
            System.out.println("b.b()");
        }
    }

    class C extends B {
        @Override
        public void m1() {
            System.out.println("C.m1()");
        }

        public void c() {
            System.out.println("c.c()");
        }
    }

    class Person extends C {
        private String name;
        private int age;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return String.format("name:%s, age:%s", name, age);
        }
    }

    @Test
    public void testBiConsumer() {
        BiConsumer<B,C> biConsumer = (a, b) -> {
            a.m1();
            b.m1();
        };

        BiConsumer<A, B> after = (a,b) -> {
            a.a();
            b.b();
        };

        BiConsumer<X, B> after2 = (x,b) -> {
            x.x();
            b.b();
        };

        BiConsumer<A, C> after3 = (a,c) -> {
            a.a();
            c.c();
        };

        BiConsumer<X, A> after4 = (x,a) -> {
            x.x();
            a.a();
        };

        BiConsumer<B,C> combineConsumer = biConsumer.andThen(after).andThen(after2).andThen(after3).andThen(after4);
        combineConsumer.accept(new B(), new C());
    }

    @Test
    public void testBiFunction() {
        BiFunction<A,B,C> biFunction = (a, b) -> {
            a.a();
            return (C)b;
        };

        C c = biFunction.apply(new A(), new C());
        Function<? super C, ? extends X> after = (c0) -> {
            c0.m1();
            c0.c();
            c0.b();
            c0.a();
            c0.x();
            return c0;
        };
        OtherTool.printSplitLine();

        BiFunction<A,B,? extends X> combine = biFunction.andThen(after);
        X x = combine.apply(new B(), new C());
    }

    @Test
    public void testBinaryOperator() {
        BinaryOperator<A> binaryOperator = (a, b) -> {
            a.a();
            b.x();
            return a;
        } ;

        //操作父类方法
        binaryOperator.apply(new A(), new A());
        OtherTool.printSplitLine();
        binaryOperator.andThen((a) -> {
            a.x();
            return new B();
        }).apply(new A(), new A());

        OtherTool.printSplitLine();
        BinaryOperator<X> binaryOperator1 = BinaryOperator.maxBy(Comparator.comparing(x -> {x.x();
            return x;}, Comparator.comparingInt(Object::hashCode)));
        X x2 = binaryOperator1.apply(new X(), new X());

        OtherTool.printSplitLine();
        Person p1 = new Person("ming", 20);
        Person p2 = new Person("zhang", 11);
        Person p3 = new Person("xia", 32);
        Person p4 = new Person("li", 10);
        List<Person> personList = Arrays.asList(p1,p2,p3,p4);

        BinaryOperator<Person> binaryOperator2 = BinaryOperator.minBy(Comparator.comparingInt(Person::getAge));
        Person px = binaryOperator2.apply(p1, p2);
        System.out.println(px);

        Person minPerson = personList.stream().reduce(binaryOperator2).orElse(null);
        System.out.println(minPerson);
        Person maxPerson = personList.stream().max(Comparator.comparingInt(Person::getAge)).orElse(null);
        System.out.println(maxPerson);
        Person maxPerson2 = personList.stream().reduce(BinaryOperator.maxBy(Comparator.comparingInt(Person::getAge))).orElse(null);
        System.out.println(maxPerson2);
    }

    @Test
    public void testBiPredicate() {
        int k = new Random().nextInt(100);
        System.out.println("k ==> " + k);
        BiPredicate<A,B> biPredicate = (a, b) -> {
            a.x();
            b.b();
            return k > 10;
        };
        boolean result = biPredicate.test(new A(), new B());
        System.out.println(result);

        BiPredicate<A,B> biPredicate1 = biPredicate.and((a,b) -> {
            a.a();
            b.m1();
            return k < 10;
        });
        boolean result1 = biPredicate1.test(new A(), new B());
        System.out.println(result1);
    }

    @Test
    public void testBooleanSupplier() {
        BooleanSupplier booleanSupplier = () -> {
            A a = new A();
            a.x();

            Person p = new Person("day etc.", 88);
            return p.getName().equals(a.getClass().getName());
        };
        System.out.println(booleanSupplier.getAsBoolean());
        OtherTool.printSplitLine();

        int random = new Random().nextInt(1039);
        System.out.println("random ==> " + random);
        BooleanSupplier booleanSupplier1 = () -> random % 2 == 0;
        System.out.println(booleanSupplier1.getAsBoolean());
    }

    @Test
    public void testConsumer() {
        Consumer<Person> consumer = A::a;
        Consumer<X> after = (e) -> {
            e.x();
            System.out.println(e.getClass());
        };

        Consumer<Person> combine = consumer.andThen(after);
        combine.accept(new Person("Up Long", 99));
    }

    @Test
    public void testDoubleBinaryOperator() {
        DoubleBinaryOperator doubleBinaryOperator = (Double::sum);
        System.out.println(doubleBinaryOperator.applyAsDouble(100.99, 200.99));

        //结合reduce使用
        DoubleBinaryOperator doubleBinaryOperator1 = ((left, right) -> left * right);
        double result = DoubleStream.of(9.9, 49.44, 48.33, 84.33).reduce(1, doubleBinaryOperator1);
        System.out.println(result);
    }

    @Test
    public void testDoubleConsumer() {
        DoubleConsumer baseConsumer = (d) -> System.out.println("base ==> " + d);
        DoubleConsumer addConsumer = d -> {
            d = d + d;
            System.out.println("add ==> " + d);
        };

        DoubleConsumer subConsumer = d -> {
            d = d - d;
            System.out.println("sub ==> " + d);
        };

        DoubleConsumer mulConsumer = d -> {
            d = d * d;
            System.out.println("mul ==> " + d);
        };

        DoubleConsumer divConsumer = d -> {
            d = d / d;
            System.out.println("div ==> " + d);
        };

        DoubleConsumer combineConsumer = baseConsumer.andThen(addConsumer).andThen(subConsumer).andThen(mulConsumer).andThen(divConsumer);
        combineConsumer.accept(100.99);
    }

    @Test
    public void testDoubleFunction() {
        //结合流使用
        DoubleFunction<Double> squareFunction = (d) -> d * d;
        DoubleStream.of(1.0, 2.0, 3.0, 4.0, 5.0)
                .mapToObj(squareFunction)
                .forEach(System.out::println);
    }

    @Test
    public void testDoublePredicate() {
        int upBound = 100;
        int downBound = -100;

        DoublePredicate isPositive = (d) -> d > 0;
        DoublePredicate isLessThanPositive = d -> d < upBound;
        DoublePredicate isLessThenNegative = d -> d < downBound;
        DoublePredicate isGreaterThen = d -> d > upBound;

        DoublePredicate isBetween = isPositive.and(isLessThanPositive);
        DoublePredicate isNotBetween = isBetween.negate();
        DoublePredicate isMatch = isGreaterThen.or(isLessThenNegative);

        double[] doubleValues = {1.0, -1, 19.8, 3847.2, -99.33, 31.9, 9384.3, 1.3, -3838.88, -100.01};
        System.out.println("is between (0, " + upBound + "):");
        DoubleStream.of(doubleValues)
                .filter(isBetween).forEach(System.out::println);
        OtherTool.printSplitLine();
        System.out.println("is not between (0, " + upBound + "):");
        DoubleStream.of(doubleValues)
                .filter(isNotBetween).forEach(System.out::println);
        OtherTool.printSplitLine();
        System.out.println("is greater(>):" + upBound + ", or lessThan(<):" + downBound + " :");
        DoubleStream.of(doubleValues)
                .filter(isMatch).forEach(System.out::println);
    }


    @Test
    public void testDoubleSupplier() {
        DoubleSupplier temperatureSensor = () -> 20.0 + Math.random() + 17.0;
        DecimalFormat df = new DecimalFormat("#.00");
        for(int i=0; i<5; i++) {
            System.out.println("模拟生成人体温度(摄氏度):" + df.format(temperatureSensor.getAsDouble()) + "°C");
        }
    }

    @Test
    public void testDoubleToIntFunction() {
        DoubleStream doubleStream = DoubleStream.of(1.3, 2.8, 4.8, 5.4, -19.3, -15.6);
        doubleStream.mapToInt(value -> (int) value).forEach(System.out::println);
    }

    @Test
    public void testDoubleToLongFunction() {
        DoubleToLongFunction celsiusToFahrenheit = (celsius) -> (long) ((celsius * 9 / 5) + 32);
        double humanCelsius = 37.3;
        System.out.println("摄氏度:" + humanCelsius + "°C 等于华式摄氏度:" + celsiusToFahrenheit.applyAsLong(humanCelsius) + "°F");
    }

    @Test
    public void testDoubleUnaryOperator() {
        DoubleUnaryOperator fahrenheitToCelsius = (fahrenheit) -> ((fahrenheit - 32) * 5)/9;
        System.out.println(fahrenheitToCelsius.applyAsDouble(99.0));

        DoubleUnaryOperator add = (d) -> d + 2.0;
        DoubleUnaryOperator mul = (d) -> d * 9;
        DoubleUnaryOperator andThen = add.andThen(mul);
        DoubleUnaryOperator compose = add.compose(mul);
        System.out.println(andThen.applyAsDouble(5.0));
        System.out.println(compose.applyAsDouble(5.0));
        System.out.println(DoubleUnaryOperator.identity().applyAsDouble(5.0));

        DoubleStream.of(1.0, 2.0, 3.0, 4.0)
                .map(add)
                .map(mul)
                .forEach(System.out::println);
    }

    @Test
    public void testFunction() {
        Function<A, B> func1 = (e) -> {
            e.m1();
            return (B)e;
        };
        A a = new B();
        A a0 = new C();
        func1.apply(a);
        func1.apply(a0);

        Function<String, B> before = (e) -> {
            System.out.println("字符串长度: " + e.length());
            return new B();
        };

        Function<B, String> after = (e) -> {
            e.b();
            return e.getClass().getName();
        };

        Function<String,B> compose = func1.compose(before);
        B b1 = compose.apply("Hello");
        b1.b();
        OtherTool.printSplitLine();
        Function<A,String> andThen = func1.andThen(after);
        String name = andThen.apply(new B());
        System.out.println(name);
        OtherTool.printSplitLine();
        Function<A, A>  func2 = Function.identity();
        System.out.println(func2.apply(new A()));
    }

    @Test
    public void testIntBinaryOperator() {
        IntBinaryOperator sum = ((left, right) -> left + right);
        IntBinaryOperator multiply = (left, right) -> left * right;

        int[] arr = {1,2,3,4,5,6};
        IntStream intStream = IntStream.of(arr);
        int addRst = intStream.reduce(sum).orElse(0);
        System.out.println(addRst);
        System.out.println(IntStream.of(arr).reduce(multiply).getAsInt());
    }

    @Test
    public void testIntConsumer() {
        AtomicInteger total = new AtomicInteger(100);

        // 先加操作
        IntConsumer current = (v) -> total.addAndGet(v);

        // 后乘操作
        IntConsumer after1 = (v) -> total.updateAndGet(v1 -> v1 * 10);

        // 打印当前结果
        IntConsumer after2 = (v) -> System.out.println("最终 total 的值: " + total.get());

        // 组合操作：先加后乘再打印
        IntConsumer combine = current.andThen(after1).andThen(after2);

        // 执行组合操作
        combine.accept(100);
    }

    @Test
    public void testIntFunction() {
        IntFunction<String> func1 = i -> {
            switch (i) {
                case 1: return "day";
                case 2: return "moon";
                case 3: return "river";
                default: return "foo";
            }
        };

        Random random = new Random();
        for (int i=0; i < 5; i++) {
            int seed = random.nextInt(5);
            System.out.println(func1.apply(seed));
        }
        OtherTool.printSplitLine();
        IntStream.of(1,2,3,4,5,6)
                .mapToObj(func1).forEach(System.out::println);
    }

    @Test
    public void testIntPredicate() {
        int upBound = 100;
        int downBound = -100;

        IntPredicate isPositive = v -> v > 0;
        IntPredicate isLessThanPositive = v -> v < upBound;
        IntPredicate isLessThanNegative = v -> v < downBound;
        IntPredicate isGreaterThan = v -> v > upBound;

        IntPredicate isBetween = isPositive.and(isLessThanPositive);
        IntPredicate isNotBetween = isBetween.negate();
        IntPredicate isMatch = isLessThanNegative.or(isGreaterThan);

        int[] arr = {-1000,-200,-100,-10,-1,0,1, 10, 100, 200, 300, 1000};
        System.out.println(String.format("isBetween(%s,%s):", 0, upBound));
        IntStream.of(arr).filter(isBetween).forEach(System.out::println);
        OtherTool.printSplitLine();
        System.out.println(String.format("isNotBetween(%s,%s):", 0, upBound));
        IntStream.of(arr).filter(isNotBetween).forEach(System.out::println);
        OtherTool.printSplitLine();
        System.out.println(String.format("< %s, > %s:", downBound, upBound));
        IntStream.of(arr).filter(isMatch).forEach(System.out::println);
    }

    @Test
    public void testIntSupplier() {
        IntSupplier func1 = () -> new Random().nextInt();
        IntConsumer func2 = (v) -> {
            System.out.println("v ==> " + v);
        };
        for (int i=0; i < 5; i++) {
            func2.accept(func1.getAsInt());
        }
        OtherTool.printSplitLine();
        IntStream.generate(() -> (int)(Math.random() * 100))
                .limit(5)
                .forEach(v -> System.out.println("ge ==> " + v));
    }

    @Test
    public void testIntToDoubleFunction() {
        //相比Function<Integer,Double>,少了拆装箱,具备更高的性能
        IntToDoubleFunction func1 = v -> Math.random() * v;
        IntStream.of(1,2,3,4,5)
                .mapToDouble(func1)
                .forEach(System.out::println);
    }

    @Test
    public void testIntToLongFunction() {
        IntToLongFunction func1 = v -> v * 10000L;
        IntStream.of(6,7,8,9,10)
                .mapToLong(func1)
                .filter(v -> (v > 60000 && v <= 80000))
                .forEach(System.out::println);
    }

    @Test
    public void testIntUnaryOperator() {
        IntUnaryOperator add = v -> v+v;
        IntUnaryOperator before = v -> v*v;
        IntUnaryOperator after = v -> v / 2;
        IntUnaryOperator compose = add.compose(before);
        IntUnaryOperator andThen = add.andThen(after);
        IntUnaryOperator identity = IntUnaryOperator.identity();
        System.out.println("identity:" + identity.applyAsInt(1000));

        int[] arr = {1,2,3,4,5};
        IntStream.of(arr).map(compose).forEach(System.out::println);
        OtherTool.printSplitLine();
        IntStream.of(arr)
                .map(compose)
                .map(andThen)
                .forEach(System.out::println);
    }

    @Test
    public void testLongBinaryOperator() {
        LongBinaryOperator add = ((l, r) -> l +r);
        LongBinaryOperator mul = (l,r) -> l * r;

        System.out.println(LongStream.of(1,2,3,4,5)
                .reduce(add)
                .orElse(0));

        System.out.println(mul.applyAsLong(100, 100));
    }

    @Test
    public void testLongConsumer() {
        AtomicLong total = new AtomicLong();
        LongConsumer func1 = v -> total.addAndGet(v);
        LongConsumer after = v -> total.updateAndGet(v1 -> v1 * 10);
        func1.accept(10);
        System.out.println(total.get());
        func1.andThen(after).accept(10);
        System.out.println(total.get());
    }

    @Test
    public void testLongFunction() {
        LongFunction<String> func1 = v -> String.format("Hello:%s", v);
        System.out.println(func1.apply(1000));
    }

    @Test
    public void testLongPredicate() {
        long upBound = 100l;
        long downBound = -100l;

        LongPredicate isPositive = v -> v > 0;
        LongPredicate isLessThanPositive = v -> v < upBound;
        LongPredicate isLessThanNegative = v -> v < downBound;
        LongPredicate isGreaterThan = v -> v > upBound;

        LongPredicate isBetween = isPositive.and(isLessThanPositive);
        LongPredicate isNotBetween = isBetween.negate();
        LongPredicate isMatch = isLessThanNegative.or(isGreaterThan);

        long[] arr = {-1000l,-200l,-100l,-10l,-1l,0l,1l, 10l, 100l, 200l, 300l, 1000l};
        System.out.println(String.format("isBetween(%s,%s):", 0l, upBound));
        LongStream.of(arr).filter(isBetween).forEach(System.out::println);
        OtherTool.printSplitLine();
        System.out.println(String.format("isNotBetween(%s,%s):", 0l, upBound));
        LongStream.of(arr).filter(isNotBetween).forEach(System.out::println);
        OtherTool.printSplitLine();
        System.out.println(String.format("< %s, > %s:", downBound, upBound));
        LongStream.of(arr).filter(isMatch).forEach(System.out::println);
    }

    @Test
    public void testLongSupplier() {
        LongSupplier func1 = () -> new Random().nextLong();
        for(int i=0; i<5; i++) {
            System.out.println(func1.getAsLong());
        }
    }

    @Test
    public void testLongToDoubleFunction() {
        LongToDoubleFunction func1 = v -> Math.random() * v ;
        System.out.println(func1.applyAsDouble(938));
    }

    @Test
    public void testLongToIntFunction() {
        LongToIntFunction func1 = v -> (int) ((v * 210) / 5);
        System.out.println(func1.applyAsInt(1));
    }

    @Test
    public void testLongUnaryOperator() {
        LongUnaryOperator add = v -> v+v;
        LongUnaryOperator before = v -> v*v;
        LongUnaryOperator after = v -> v / 2;
        LongUnaryOperator compose = add.compose(before);
        LongUnaryOperator andThen = add.andThen(after);
        LongUnaryOperator identity = LongUnaryOperator.identity();
        System.out.println("identity:" + identity.applyAsLong(1000));

        long[] arr = {1,2,3,4,5};
        LongStream.of(arr).map(compose).forEach(System.out::println);
        OtherTool.printSplitLine();
        LongStream.of(arr)
                .map(compose)
                .map(andThen)
                .forEach(System.out::println);
    }
}
