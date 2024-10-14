package com.future.function.vision.tool;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.function.BiConsumer;

public class OtherTool {

    private OtherTool() {

    }

    public static class Person {
        int age;
        public Person(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return String.format("person age:" + age);
        }
    }

    public static int addInteger(List<Integer> integerList) {
        return integerList.stream().mapToInt(i -> i).sum();
    }

    public static double addDouble(List<Double> doubleList) {
        return doubleList.stream().mapToDouble(i -> i).sum();
    }

    public static <T extends Number> T addNumber(List<T> list) {
        T sum = null;
        T t = list.get(0);
        if(t instanceof Integer) {
            Integer intSum = 0;
            for(T item : list) {
                intSum += item.intValue();
            }
            sum = (T)intSum;
        } else if(t instanceof Double) {
            Double doubleSum = 0.0;
            for(T item : list) {
                doubleSum += item.doubleValue();
            }
            sum = (T) doubleSum;
        }
        return sum;
    }

    public static <T> Number addNumbers(List<? super T> numberList) {
        if (numberList.isEmpty()) {
            return 0; // 返回0作为默认值
        }

        // 检查类型并计算和
        if (numberList.get(0) instanceof Integer) {
            int sum = 0;
            for (Object num : numberList) {
                sum += (Integer) num; // 强制类型转换
            }
            return sum; // 返回int类型的和
        } else if (numberList.get(0) instanceof Double) {
            double sum = 0.0;
            for (Object num : numberList) {
                sum += (Double) num; // 强制类型转换
            }
            return sum; // 返回double类型的和
        }

        return 0; // 默认返回
    }


    public static BiConsumer<Integer, Integer> getIntegerIntegerBiConsumer() {
        BiConsumer<Integer, Integer> func2 = (t1, t2) -> System.out.printf("Add:%s%n", (t1 + t2));
        BiConsumer<Number, Number> func3 = (t1,t2) -> System.out.printf("Multiply:%s%n", (t1.doubleValue() * t2.doubleValue()));
        BiConsumer<Integer,Integer> combined = func2.andThen(func3);
        BiConsumer<Integer, Integer> func4 = (t1, t2) -> System.out.printf("Subtraction:%s%n", t1 - t2);
        return combined.andThen(func4);
    }

    public static void printSplitLine() {
        System.out.println("==============================Split Line==============================");
    }


    public static void outputFieldInfo(Field field) {
        System.out.println("field: " + field);
        Type genericType = field.getGenericType();
        System.out.println("genericType: " + genericType);

        if(genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            System.out.println("parameterizedType: " + parameterizedType);

            Type[] actualType = parameterizedType.getActualTypeArguments();
            for(Type type : actualType) {
                System.out.println("实际参数类型: " + type.getTypeName());
            }
            System.out.println("原始类型: " + parameterizedType.getRawType());
            System.out.println("OwnType: " + parameterizedType.getOwnerType());
        } else if(genericType instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) genericType;
            System.out.println(String.format("typeVariable name:%s, typeName:%s", typeVariable.getName(), typeVariable.getTypeName()));

            Type[] bounds = typeVariable.getBounds();
            for(Type bound : bounds) {
                System.out.println("bound:" + bound.getTypeName());
            }
        } else {
            System.out.println("foo");
        }
    }

    public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        return clazz.getDeclaredField(fieldName);
    }
}
