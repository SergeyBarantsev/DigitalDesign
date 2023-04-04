package com.digdes.school;

import java.util.*;


public class JavaSchoolStarter {
    private final List<Map<String, Object>> list = new ArrayList<>();

    //Дефолтный конструктор
    public JavaSchoolStarter() {
    }

    public List<Map<String, Object>> execute(String request) throws Exception {
        if (request.trim().toUpperCase().startsWith("INSERT")) {
            return insertMethod(request);
        }
        if (request.trim().toUpperCase().startsWith("UPDATE")) {
            return updateMethod(request);
        }
        if (request.trim().toUpperCase().startsWith("SELECT")) {
            return searchMethod(request);
        }
        if (request.trim().toUpperCase().startsWith("DELETE")) {
            return deleteMethod(request);
        }
        throw new Exception("Ошибка синтаксиса запроса");
    }

    /**
     * Вставка данных в коллекцию
     *
     * @param str - запрос
     * @return - список элементов в коллекции, которые были добавлены
     */
    private List<Map<String, Object>> insertMethod(String str) throws Exception {
        List<Map<String, Object>> insertList = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();
        row.put("id", null);
        row.put("lastName", null);
        row.put("age", null);
        row.put("cost", null);
        row.put("active", null);
        if (str.toUpperCase().trim().replaceAll(" ", "").contains("INSERTVALUES")) {
            String[] values = str.split(",");
            Methods.defineValues(values, row);
            if (row.get("id") == null && row.get("lastName") == null && row.get("age") == null && row.get("cost") == null && row.get("active") == null) {
                throw new Exception("Все значения в ячейках в одной строчке пустыми быть не могут.");
            }
            insertList.add(row);
            list.add(row);
        } else {
            throw new Exception("Ошибка синтаксиса запроса");
        }
        return insertList;
    }

    /**
     * Изменение коллекции
     *
     * @param str - запрос
     * @return - список элементов в коллекции, которые были изменены
     */
    private List<Map<String, Object>> updateMethod(String str) throws Exception {
        List<Map<String, Object>> updateList = new ArrayList<>();
        if (str.toUpperCase().trim().replaceAll(" ", "").contains("UPDATEVALUES")) {
            String[] array = str.split("[Ww][Hh][Ee][Rr][Ee]");
            String[] values = array[0].split(",");
            String conditions = array[1];
            for (Map<String, Object> i : list) {
                if (Methods.checked(i, conditions)) {
                    updateList.add(i);
                }
            }
            for (Map<String, Object> j : updateList) {
                Methods.defineValues(values, j);
            }
        } else {
            throw new Exception("Ошибка синтаксиса запроса");
        }
        return updateList;
    }

    /**
     * Удаление элемента из коллекции
     *
     * @param str - запрос
     * @return - список элементов в коллекции, которые были удалены
     */
    private List<Map<String, Object>> deleteMethod(String str) throws Exception {
        List<Map<String, Object>> deleteList = new ArrayList<>();
        if (str.toUpperCase().trim().replaceAll(" ", "").contains("WHERE")) {
            String[] array = str.split("[Ww][Hh][Ee][Rr][Ee]");
            String conditions = array[1];
            List<Map<String, Object>> list1 = new ArrayList<>(list);
            for (Map<String, Object> i : list1) {
                if (Methods.checked(i, conditions)) {
                    deleteList.add(i);
                    list.remove(i);
                }
            }
        } else {
            throw new Exception("Ошибка синтаксиса запроса");
        }
        return deleteList;
    }

    /**
     * Поиск элементов в коллекции
     *
     * @param str - запрос
     * @return - список элементов в коллекции, которые были найдены
     */
    private List<Map<String, Object>> searchMethod(String str) throws Exception {
        List<Map<String, Object>> searchList = new ArrayList<>();
        if (str.toUpperCase().trim().replaceAll(" ", "").contains("WHERE")) {
            String[] array = str.split("[Ww][Hh][Ee][Rr][Ee]");
            String conditions = array[1];
            for (Map<String, Object> i : list) {
                if (Methods.checked(i, conditions)) {
                    searchList.add(i);
                }
            }
        } else {
            return list;
        }
        return searchList;
    }
}