package com.digdes.school;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        try {
//Вставка строки в коллекцию
            List<Map<String, Object>> result1 = starter.execute("insert VALUES 'lAStName' = 'Баранцев Сергей' , 'id' = 1, 'cost' = 25, 'age' = 15, 'active' = true");
            System.out.println("1" + result1);
            List<Map<String, Object>> result2 = starter.execute("INSERT VALUES 'lastName' = 'Малявка Юлиана', 'id'=2, 'cost' = 25.1, 'age'=25, 'active'=  true");
            System.out.println("2" + result2);
            List<Map<String, Object>> result3 = starter.execute("INSERT VALUES 'lastName' = 'Петрова' , 'id'=3, 'age'=40, 'active'=true, 'cost'=3.2");
            List<Map<String, Object>> result10 = starter.execute("INSERT VALUES 'lastName' = 'Федоров' , 'id'=4, 'age'=17, 'active'=false, 'cost'=80.2");
            System.out.println("3" + result3);
            List<Map<String, Object>> result0 = starter.execute("SELECT");
            System.out.println("0" + result0);
////////Изменение значения которое выше записывали
            List<Map<String, Object>> result4 = starter.execute("delete  where 'age' >25");
            System.out.println("4" + result4);
            List<Map<String, Object>> result00 = starter.execute("SELECT");
            System.out.println("0" + result00);
            List<Map<String, Object>> result5 = starter.execute("UPDATE VALUES 'cost'=44.1 where 'cost' > 25 or 'age' = 40 and 'id'=3 or 'lastname' ilike '%F% and 'age'<20'");
            System.out.println("5" + result5);
            List<Map<String, Object>> result000 = starter.execute("SELECT");
            System.out.println("0" + result000);
            List<Map<String, Object>> result6 = starter.execute(" SELECT WHERE  'lastName' ilike '%Ф%'");
            System.out.println("6" + result6);
//Получение всех данных из коллекции (т.е. в данном примере вернется 1 запись)
            List<Map<String, Object>> result7 = starter.execute("SELECT");
            System.out.println("7" + result7);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}