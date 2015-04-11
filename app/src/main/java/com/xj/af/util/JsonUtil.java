package com.xj.af.util;

import org.json.JSONObject;

import java.lang.reflect.Method;

public class JsonUtil {

    /**
     * 将json转成对象
     *
     * @param str    json字符串
     * @param classT Entity.class
     * @return Entity
     */
    public static <T> T getEntity(String str, Class<T> classT) {
        T objectT = null;
        try {
            objectT = classT.newInstance();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            JSONObject jo = new JSONObject(str);
            Method methods[] = classT.getMethods();

            for (Method method : methods) {
                String methodName = method.getName();
                String field;
                if (methodName.startsWith("get")) {
                    field = methodName.substring(3, 4).toLowerCase()
                            + methodName.substring(4, methodName.length());
                    try {
                        if (method.getReturnType() == String.class) {
                            Method setMethod = classT.getDeclaredMethod(
                                    "set"
                                            + methodName.substring(3,
                                            methodName.length()),
                                    String.class);
                            String value = jo.getString(field);
                            setMethod.invoke(objectT, value);
                        } else if (method.getReturnType() == int.class) {
                            int value = jo.getInt(field);
                            Method setMethod = classT.getDeclaredMethod(
                                    "set"
                                            + methodName.substring(3,
                                            methodName.length()),
                                    int.class);
                            setMethod.invoke(objectT, value);
                        } else if (method.getReturnType() == Integer.class) {
                            Integer value = jo.getInt(field);
                            Method setMethod = classT.getDeclaredMethod(
                                    "set"
                                            + methodName.substring(3,
                                            methodName.length()),
                                    Integer.class);
                            setMethod.invoke(objectT, value);
                        } else if (method.getReturnType() == Long.class) {
                            Long value = jo.getLong(field);
                            Method setMethod = classT.getDeclaredMethod(
                                    "set"
                                            + methodName.substring(3,
                                            methodName.length()),
                                    Long.class);
                            setMethod.invoke(objectT, value);
                        } else if (method.getReturnType() == long.class) {
                            Long value = jo.getLong(field);
                            Method setMethod = classT.getDeclaredMethod(
                                    "set"
                                            + methodName.substring(3,
                                            methodName.length()),
                                    long.class);
                            setMethod.invoke(objectT, value);
                        } else if (method.getReturnType() == double.class) {
                            double value = jo.getDouble(field);
                            Method setMethod = classT.getDeclaredMethod(
                                    "set"
                                            + methodName.substring(3,
                                            methodName.length()),
                                    double.class);
                            setMethod.invoke(objectT, value);
                        } else if (method.getReturnType() == Double.class) {
                            Double value = jo.getDouble(field);
                            Method setMethod = classT.getDeclaredMethod(
                                    "set"
                                            + methodName.substring(3,
                                            methodName.length()),
                                    Double.class);
                            setMethod.invoke(objectT, value);
                        }
                    } catch (Exception e) {
                        System.out.println("error:" + e.getMessage());
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return objectT;
    }
}
