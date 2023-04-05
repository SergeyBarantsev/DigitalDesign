package com.digdes.school;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Methods {
    /**
     * Вспомогательные методы
     */

    //Определение добавляемых или изменяемых значений
    static void defineValues(String[] values, Map<String, Object> row) throws Exception {
        for (String i : values) {
            boolean isContain = false;
            if (i.toLowerCase().contains("'id'")) {
                if (!i.contains("=")) {
                    throw new Exception("Использован неверный оператор, для присвоения значений используйте '='");
                }
                String str = i.substring(i.indexOf("=") + 1).replaceAll(" ", "");
                isContain = true;
                if (str.toLowerCase().contains("null")) {
                    row.put("id", null);
                } else {
                    Long id = Long.valueOf(str);
                    row.put("id", id);
                }
            }
            if (i.toLowerCase().contains("'lastname'")) {
                if (!i.contains("=")) {
                    throw new Exception("Использован неверный оператор, для присвоения значений используйте '='");
                }
                String str = i.substring(i.indexOf("=") + 1).trim();
                isContain = true;
                if (str.toLowerCase().contains("null")) {
                    row.put("lastName", null);
                } else {
                    String lastName = str.replaceAll("'", "");
                    if (lastName.matches("[A-aZ-zА-аЯ-я\\s]+")) {
                        row.put("lastName", lastName);
                    } else {
                        throw new Exception("Символы <>!=;,.\"'@#$%^&*()_-=+ не поддерживаются для ввода в поле lastName");
                    }
                }
            }
            if (i.toLowerCase().contains("'age'")) {
                if (!i.contains("=")) {
                    throw new Exception("Использован неверный оператор, для присвоения значений используйте '='");
                }
                String str = i.substring(i.indexOf("=") + 1).replaceAll(" ", "");
                isContain = true;
                if (str.toLowerCase().contains("null")) {
                    row.put("age", null);
                } else {
                    Long age = Long.valueOf(str);
                    row.put("age", age);
                }
            }
            if (i.toLowerCase().contains("'cost'")) {
                if (!i.contains("=")) {
                    throw new Exception("Использован неверный оператор, для присвоения значений используйте '='");
                }
                String str = i.substring(i.indexOf("=") + 1).replaceAll(" ", "");
                isContain = true;
                if (str.toLowerCase().contains("null")) {
                    row.put("cost", null);
                } else {
                    Double cost = Double.valueOf(str);
                    row.put("cost", cost);
                }
            }
            if (i.toLowerCase().contains("'active'")) {
                if (!i.contains("=")) {
                    throw new Exception("Использован неверный оператор, для присвоения значений используйте '='");
                }
                String str = i.substring(i.indexOf("=") + 1).replaceAll(" ", "");
                isContain = true;
                if (str.toLowerCase().contains("null")) {
                    row.put("active", null);
                } else {
                    if (str.matches("[tf][ra][ul][es]e?")) {
                        Boolean isActive = Boolean.valueOf(str);
                        row.put("active", isActive);
                    } else {
                        throw new Exception("Для 'active' необходимо указать true/false");
                    }
                }
            }
            if (!isContain) {
                throw new Exception("Некорректно указана колонка для внесения значения");
            }
        }
    }


    //Проверка каждой строки списка на соответсвие условиям запроса
    static boolean checked(Map<String, Object> list, String conditions) throws Exception {
        List<String> listOfOrConditions = new ArrayList<>(List.of(conditions.split("[Oo][Rr]")));
        List<List<String>> listOfAndConditions = new ArrayList<>();
        List<String> specialList = new ArrayList<>(listOfOrConditions);
        for (String str : specialList) {
            if (str.toLowerCase().contains("and")) {
                String[] and = str.split("[Aa][Nn][Dd]");
                listOfAndConditions.add(List.of(and));
                listOfOrConditions.remove(str);
            }
        }
        Set<Boolean> checkedOr = new HashSet<>();

        if (!listOfAndConditions.isEmpty()) {
            for (List<String> listOfAndCondition : listOfAndConditions) {
                Set<Boolean> checkedAnd = new HashSet<>();
                defineCondition(list, checkedAnd, listOfAndCondition);
                if (!checkedAnd.isEmpty() && !checkedAnd.contains(false)) {   //проверка AND условий
                    return true;
                }
            }
        }
        defineCondition(list, checkedOr, listOfOrConditions);   //проверка OR условий
        return !checkedOr.isEmpty() && checkedOr.contains(true);
    }

    //Определение условий
    private static void defineCondition
    (Map<String, Object> list, Set<Boolean> checkSet, List<String> listOfCondition) throws Exception {
        Long listId = (Long) list.get("id");
        Long listAge = (Long) list.get("age");
        Double listCost = (Double) list.get("cost");
        String listLastName = (String) list.get("lastName");
        Boolean listActive = (Boolean) list.get("active");
        for (String condition : listOfCondition) {
            if (condition.toLowerCase().contains("'id'")) {
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(condition);
                if (matcher.find()) {
                    Long condId = Long.valueOf(condition.substring(matcher.start(), matcher.end()));
                    if (condition.replaceAll(" ", "").matches("'\\w+'=(\\d+)")) {
                        checkSet.add(Objects.equals(listId, condId));
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'!=(\\d+)")) {
                        checkSet.add(!(Objects.equals(listId, condId)));
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'>=(\\d+)")) {
                        checkSet.add(listId != null && listId >= condId);
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'<=(\\d+)")) {
                        checkSet.add(listId != null && listId <= condId);
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'>(\\d+)")) {
                        checkSet.add(listId != null && listId > condId);
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'<(\\d+)")) {
                        checkSet.add(listId != null && listId < condId);
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'([Ii]?[Ll][Ii][Kk][Ee])(\\d+)")) {
                        throw new Exception("Данный оператор не поддерживается для 'id'");
                    }
                } else {
                    pattern = Pattern.compile("[Nn][Uu][Ll][Ll]");
                    matcher = pattern.matcher(condition);
                    if (matcher.find()) {
                        Long condId = null;
                        if (condition.replaceAll(" ", "").matches("'\\w+'=(null)")) {
                            checkSet.add(Objects.equals(listId, condId));
                        }
                        if (condition.replaceAll(" ", "").matches("'\\w+'!=(null)")) {
                            checkSet.add(!(Objects.equals(listId, condId)));
                        }
                        if (condition.replaceAll(" ", "").matches("'\\w+'([Ii]?[Ll][Ii][Kk][Ee]|[><]=?)(null)"))
                            throw new Exception("Данный оператор не поддерживается для null");
                    }
                }
            }
            if (condition.toLowerCase().contains("'age'")) {
                Pattern pattern = Pattern.compile("(\\d+)");
                Matcher matcher = pattern.matcher(condition);
                if (matcher.find()) {
                    Long condAge = Long.valueOf(condition.substring(matcher.start(), matcher.end()));
                    if (condition.replaceAll(" ", "").matches("'\\w+'=(\\d+)")) {
                        checkSet.add(Objects.equals(listAge, condAge));
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'!=(\\d+)")) {
                        checkSet.add(!(Objects.equals(listAge, condAge)));
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'>=(\\d+)")) {
                        checkSet.add(listAge != null && listAge >= condAge);
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'<=(\\d+)")) {
                        checkSet.add(listAge != null && listAge <= condAge);
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'>(\\d+)")) {
                        checkSet.add(listAge != null && listAge > condAge);
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'<(\\d+)")) {
                        checkSet.add(listAge != null && listAge < condAge);
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'([Ii]?[Ll][Ii][Kk][Ee])(\\d+)")) {
                        throw new Exception("Данный оператор не поддерживается для 'age'");
                    }
                } else {
                    pattern = Pattern.compile("[Nn][Uu][Ll][Ll]");
                    matcher = pattern.matcher(condition);
                    if (matcher.find()) {
                        Long condAge = null;
                        if (condition.replaceAll(" ", "").matches("'\\w+'=(null)")) {
                            checkSet.add(Objects.equals(listAge, condAge));
                        }
                        if (condition.replaceAll(" ", "").matches("'\\w+'!=(null)")) {
                            checkSet.add(!(Objects.equals(listAge, condAge)));
                        }
                        if (condition.replaceAll(" ", "").matches("'\\w+'([Ii]?[Ll][Ii][Kk][Ee]|[><]=?)(null)"))
                            throw new Exception("Данный оператор не поддерживается для null");
                    }
                }
            }
            if (condition.toLowerCase().contains("'cost'")) {
                Pattern pattern = Pattern.compile("\\d+\\.?\\d?");
                Matcher matcher = pattern.matcher(condition);
                if (matcher.find()) {
                    Double condCost = Double.valueOf(condition.substring(matcher.start(), matcher.end()));
                    if (condition.replaceAll(" ", "").matches("'\\w+'=(\\d+\\.?\\d?)")) {
                        checkSet.add(Objects.equals(listCost, condCost));
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'!=(\\d+\\.?\\d?)")) {
                        checkSet.add(!(Objects.equals(listCost, condCost)));
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'>=(\\d+\\.?\\d?)")) {
                        checkSet.add(listCost != null && listCost >= condCost);
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'<=(\\d+\\.?\\d?)")) {
                        checkSet.add(listCost != null && listCost <= condCost);
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'>(\\d+\\.?\\d?)")) {
                        checkSet.add(listCost != null && listCost > condCost);
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'<(\\d+\\.?\\d?)")) {
                        checkSet.add(listCost != null && listCost < condCost);
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'([Ii]?[Ll][Ii][Kk][Ee])(\\d+\\.?\\d?)")) {
                        throw new Exception("Данный оператор не поддерживается для 'cost'");
                    }
                } else {
                    pattern = Pattern.compile("[Nn][Uu][Ll][Ll]");
                    matcher = pattern.matcher(condition);
                    if (matcher.find()) {
                        Double condCost = null;
                        if (condition.replaceAll(" ", "").matches("'\\w+'=(null)")) {
                            checkSet.add(Objects.equals(listCost, condCost));
                        }
                        if (condition.replaceAll(" ", "").matches("'\\w+'!=(null)")) {
                            checkSet.add(!(Objects.equals(listCost, condCost)));
                        }
                        if (condition.replaceAll(" ", "").matches("'\\w+'([Ii]?[Ll][Ii][Kk][Ee]|[><]=?)(null)"))
                            throw new Exception("Данный оператор не поддерживается для null");
                    }
                }
            }
            if (condition.toLowerCase().contains("'lastname'")) {
                String str;
                try {
                    str = condition.split("i?[Ll][Ii][Kk][Ee]|!?=|[<>]=?")[1].trim();
                } catch (Exception ex) {
                    throw new Exception("В lastName отсутствует условие или оператор");
                }
                Pattern pattern = Pattern.compile("(%?[A-aZ-zА-аЯ-я\\s]+%?)");
                Matcher matcher = pattern.matcher(str);
                if (matcher.find()) {
                    String condLastname = str.substring(matcher.start(), matcher.end());
                    if (condLastname.toLowerCase().matches("'?[Nn][Uu][Ll][Ll]'?")) {
                        if (condition.replaceAll(" ", "").matches("'\\w+'='?(null)'?")) {
                            checkSet.add(Objects.equals(listLastName, null));
                        }
                        if (condition.replaceAll(" ", "").matches("'\\w+'!='?(null)'?")) {
                            checkSet.add(!(Objects.equals(listLastName, null)));
                        }
                        if (condition.replaceAll(" ", "").matches("'\\w+'([Ii]?[Ll][Ii][Kk][Ee]|[><]=?)'?(null)'?"))
                            throw new Exception("Данный оператор не поддерживается для null");
                    } else {
                        if (condition.replaceAll(" ", "").matches("'\\w+'='([A-aZ-zА-аЯ-я\\s]+)'")) {
                            checkSet.add(Objects.equals(listLastName, condLastname));
                        }
                        if (condition.replaceAll(" ", "").matches("'\\w+'!='([A-aZ-zА-аЯ-я\\s]+)'")) {
                            checkSet.add(!(Objects.equals(listLastName, condLastname)));
                        }
                        if ((condition.replaceAll(" ", "").matches("'\\w+'!?='(%[A-aZ-zА-аЯ-я\\s]+)'") |
                                (condition.replaceAll(" ", "").matches("'\\w+'!?='([A-aZ-zА-аЯ-я\\s]+%)'")) |
                                (condition.replaceAll(" ", "").matches("'\\w+'!?='(%[A-aZ-zА-аЯ-я\\s]+%)'")))) {
                            throw new Exception("При использовании % применяйте операторы LIKE или ILIKE");
                        }
                        if (condition.replaceAll(" ", "").matches("'\\w+'([Ll][Ii][Kk][Ee])'(%?[A-aZ-zА-аЯ-я\\s]+%?)'")) {
                            if (condLastname.startsWith("%") && condLastname.endsWith("%")) {
                                String reg = condLastname.replaceAll("%", "");
                                checkSet.add(listLastName != null && (listLastName.contains(reg) | listLastName.endsWith(reg) | listLastName.startsWith(reg)));
                            }
                            if (condLastname.startsWith("%") && !condLastname.endsWith("%")) {
                                String reg = condLastname.replaceAll("%", "");
                                checkSet.add(listLastName != null && listLastName.endsWith(reg));
                            }
                            if (condLastname.endsWith("%") && !condLastname.startsWith("%")) {
                                String reg = condLastname.replaceAll("%", "");
                                checkSet.add(listLastName != null && listLastName.startsWith(reg));
                            }
                        }
                        if (condition.replaceAll(" ", "").matches("'\\w+'([Ii][Ll][Ii][Kk][Ee])'(%?[A-aZ-zА-аЯ-я\\s]+%?)'")) {
                            if (condLastname.startsWith("%") && condLastname.endsWith("%")) {
                                String reg = condLastname.toLowerCase().replaceAll("%", "");
                                checkSet.add(listLastName != null && (listLastName.toLowerCase().contains(reg) | listLastName.toLowerCase().endsWith(reg) | listLastName.toLowerCase().startsWith(reg)));
                            }
                            if (condLastname.startsWith("%") && !condLastname.endsWith("%")) {
                                String reg = condLastname.toLowerCase().replaceAll("%", "");
                                checkSet.add(listLastName != null && listLastName.toLowerCase().endsWith(reg));
                            }
                            if (condLastname.endsWith("%") && !condLastname.startsWith("%")) {
                                String reg = condLastname.toLowerCase().replaceAll("%", "");
                                checkSet.add(listLastName != null && listLastName.toLowerCase().startsWith(reg));
                            }
                        }
                        if (condition.replaceAll(" ", "").matches("'\\w+'[><]=?'(%?[A-aZ-zА-аЯ-я\\s]+%?)'")) {
                            throw new Exception("Данный оператор не поддерживается для 'lastName'");
                        }
                    }
                }
            }
            if (condition.toLowerCase().contains("'active'")) {
                Pattern pattern = Pattern.compile("[tf][ra][ul][es]e?");
                Matcher matcher = pattern.matcher(condition);
                if (matcher.find()) {
                    Boolean condActive = Boolean.valueOf(condition.substring(matcher.start(), matcher.end()));
                    if (condition.replaceAll(" ", "").matches("'\\w+'=\\w+")) {
                        checkSet.add(Objects.equals(listActive, condActive));
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'!=\\w+")) {
                        checkSet.add(!(Objects.equals(listActive, condActive)));
                    }
                    if (condition.replaceAll(" ", "").matches("'\\w+'([Ii]?[Ll][Ii][Kk][Ee]|[><]=?)(\\w+)")) {
                        throw new Exception("Данный оператор не поддерживается для 'active'");
                    }
                } else {
                    pattern = Pattern.compile("[Nn][Uu][Ll][Ll]");
                    matcher = pattern.matcher(condition);
                    if (matcher.find()) {
                        Boolean condActive = null;
                        if (condition.replaceAll(" ", "").matches("'\\w+'=(null)")) {
                            checkSet.add(Objects.equals(listActive, condActive));
                        }
                        if (condition.replaceAll(" ", "").matches("'\\w+'!=(null)")) {
                            checkSet.add(!(Objects.equals(listActive, condActive)));
                        }
                        if (condition.replaceAll(" ", "").matches("'\\w+'([Ii]?[Ll][Ii][Kk][Ee]|[><]=?)(null)"))
                            throw new Exception("Данный оператор не поддерживается для null");
                    }
                }
            }
        }
    }
}