package com.sjp.domain.usecase;


import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

final class ClassUtil {

    //======================================================================
    // Constants
    //======================================================================

    private static final String TYPE_NAME_PREFIX = "class ";

    //======================================================================
    // Public Methods
    //======================================================================

    /**
     * @param target      TypeHandler define class.
     * @param memberArray 1.class, 2.class ...
     * @return Type {@link Type}
     */
    public static Type getInnerClassExtendsType(Class<?> target, Class<?>... memberArray) {
        Map<String, Type> typeMap = getInnerClassExtendsTypes(target);
        return getMatchingType(typeMap, memberArray);
    }

    /**
     * @param memberArray ex) 1.class, 2.class ...
     * @return ex) com.xxx.1'<com.xxx.2'>
     */
    public static String toExtendsName(Class<?>[] memberArray) {
        ArrayList<String> list = new ArrayList<>();
        for (Class<?> clazz : memberArray) {
            list.add(clazz.getName());
        }
        return toExtendsName(list);
    }

    /**
     * @param list ArrayList'<Class.getName()'>
     * @return ex) com.xxx.1'<com.xxx.2'>
     */
    public static String toExtendsName(ArrayList<String> list) {
        String extendsValue = "";
        if (list != null && list.size() > 0) {
            for (int i = list.size() - 1; i >= 0; i--) {
                String name = list.get(i);
                if (extendsValue.length() == 0) {
                    extendsValue = name;
                } else {
                    if (name.equals(Object.class.getName()) == false) {
                        extendsValue = "<" + extendsValue + ">";
                        extendsValue = name + extendsValue;
                    }
                }
            }
        }
        return extendsValue;
    }

    /**
     * @param memberArray ex) 1.class, 2.class ...
     * @return ex) 1'<2'>
     */
    @SuppressWarnings("unused")
    public static String toExtendsSimpleName(Class<?>[] memberArray) {
        ArrayList<String> list = new ArrayList<>();
        for (Class<?> clazz : memberArray) {
            list.add(clazz.getSimpleName());
        }
        return toExtendsName(list);
    }


    /**
     * @param typeMap     Type map( com.xxx.1<co.xxx.2> -> Type {@link Type})
     * @param memberArray 1.class, 2.class ...
     * @return Type {@link Type}
     */
    public static Type getMatchingType(Map<String, Type> typeMap, Class<?>... memberArray) {
        try {
            if (typeMap.containsKey(toExtendsName(memberArray)) == true) {
                return typeMap.get(toExtendsName(memberArray));
            } else if (typeMap.containsKey(toExtendsSimpleName(memberArray)) == true) {
                return typeMap.get(toExtendsSimpleName(memberArray));
            }
        } catch (Exception e) {
            //Nothing
        }
        return Object.class;
    }


    /**
     * @param target TypeHandler define class.
     * @return Type map( com.xxx.1<co.xxx.2> -> Type {@link Type})
     */
    public static Map<String, Type> getInnerClassExtendsTypes(Class<?> target) {
        Map<String, Type> typeMap = new HashMap<>();
        int clazzIndex = 0;
        while (true) {
            try {
                String className = target.getName() + "$" + clazzIndex++;
                Class<?> clazz = Class.forName(className);
                Type types[] = getGenericSuperclassParameterizedTypesInternal(clazz);
                typeMap.put(types[0].toString(), types[0]);
            } catch (ClassNotFoundException notFound) {
                break;
            } catch (Exception e) {
                break;
            }
        }

        return typeMap;
    }

    public static Class<?> getReclusiveGenericClass(Class<?> clazz, int index) {
        Class<?> targetClass = clazz;
        while (targetClass != null) {
            Class<?> genericClass = getGenericClass(targetClass, index);
            if (genericClass != null) {
                return genericClass;
            }
            targetClass = targetClass.getSuperclass();
        }
        return null;
    }

    public static Class<?> getGenericClass(Class<?> clazz, int index) {
        Type types[] = getGenericSuperclassParameterizedTypesInternal(clazz);
        if ((types != null) && (types.length > index)) {
            try {
                return getClassInternal(types[index]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Type[] getGenericSuperclassParameterizedTypes(Class<?> clazz) {
        return getGenericSuperclassParameterizedTypesInternal(clazz);
    }

    public static Class<?> getClass(Type type) throws ClassNotFoundException {
        return getClassInternal(type);
    }

    public static String getClassName(Type type) {
        if (type == null) {
            return "";
        }
        String className = type.toString();
        if (className.startsWith(TYPE_NAME_PREFIX)) {
            className = className.substring(TYPE_NAME_PREFIX.length());
        }
        return className;
    }

    @SuppressWarnings("unchecked")
    public static Method getEnclosingMethod(@NonNull Object object) {
        return object.getClass().getEnclosingMethod();
    }

    //======================================================================
    // Methods
    //======================================================================

    static Type[] getGenericSuperclassParameterizedTypesInternal(Class<?> target) {
        Type[] types = getGenericSuperclassType(target);
        if (types.length > 0 && types[0] instanceof ParameterizedType) {
            return ((ParameterizedType) types[0]).getActualTypeArguments();
        }
        return null;
    }

    static Type[] getGenericSuperclassType(Class<?> target) {
        if (target == null) {
            return new Type[0];
        }

        Type type = target.getGenericSuperclass();
        if (type != null) {
            if (type instanceof ParameterizedType) {
                return new Type[]{type};
            }
        }
        return new Type[0];
    }

    static Class<?> getClassInternal(Type type) throws ClassNotFoundException {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return getClassInternal(((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> componentClass = getClassInternal(componentType);
            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            }
        }
        String className = getClassName(type);
        if (className == null || className.isEmpty()) {
            return null;
        }
        return Class.forName(className);
    }
}
