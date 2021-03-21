import java.util.*;

class Scratch {

    private static int counter;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.next();

        if (!validate(str)) {
            System.err.println("Неправильна введена строка, все не так, давайте по новой");
            System.exit(1);
        }

        long x = System.currentTimeMillis();
        counter = countBrackets(str);
        start2(str);
        System.out.println(System.currentTimeMillis() - x);
    }

    //Валидация строки, проверка на ее правильность
    public static boolean validate(String string) {
        int countOfBrackets = 0;

        StringBuilder number = new StringBuilder();
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char x = chars[i];
            //Проверяет, явлется ли символ буквой или числом
            if (Character.isLetter(x) || Character.isDigit(x) || x == ('[') || x == (']')) {
                //Идет подсчет кол-ва открывающихся скобок, увеличивая countOfBrackets на 1
                if (x == ('[')) {
                    countOfBrackets++;
                    try {
                        //Попытка превратить символы в число и подсчет кол-ва цифр в данной комбинации
                        Integer.parseInt(number.toString());        //Вопрос, нужно ли было добавлять проверку на 0 или 1, хотя это все тоже число, и в этом нет проблем?
                    } catch (Exception e) {                         //Это просто сделать int x = Integer.parseInt(number.toString()); if(x == 0 || x == 1) return false;
                        //System.out.println("here exception - " + e.getMessage());
                        return false;
                    }
                    //Идет подсчет кол-ва закрывающихся скобок, уменьшая countOfBrackets на 1
                }
                if (x == (']')) {
                    countOfBrackets--;
                }

                //Проверка символа на число
                if (Character.isDigit(x)) {
                    number.append(x);
                    //Проверка на след символ
                    if (!(i < chars.length - 1 && (Character.isDigit(chars[i + 1]) || chars[i + 1] == '['))) {
                        return false;
                    }
                } else {
                    number = new StringBuilder();
                }

                //Финальная проверка на правильность строки

            } else {
                //System.out.println("Что-то не так с символом");
                return false;
            }

        }

        if (countOfBrackets != 0) {
            System.out.println(countOfBrackets);
            System.out.println("Не прошло финальную проверку");
            return false;
        }
        return true;
    }

    //Получить кол-во открывающихся скобочек
    public static int countBrackets(String string) {
        int count = 0;
        for (Character x : string.toCharArray()) if (x.equals('[')) count++;
        return count;
    }

    //Новый (2) вариант работы программы
    public static String start2(String string) {
        if (counter > 0) {
            counter--;
            StringBuilder newString = new StringBuilder(string);
            int indexOfClosingBracket = 0;
            int indexOfOpeningBracket = 0;
            char[] chars = string.toCharArray();

            //Поиск индекса первой закрывающейся скобки
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == (']')) {
                    indexOfClosingBracket = i;
                    break;
                }
            }
            //System.out.println("indexOfClosingBracket - " + indexOfClosingBracket);

            //Поиск первой близжащей открывающейся скобки для закрывающей
            for (int i = indexOfClosingBracket; i >= 0; i--) {
                if (chars[i] == ('[')) {
                    indexOfOpeningBracket = i;
                    break;
                }
            }
            //System.out.println("indexOfOpeningBracket - " + indexOfOpeningBracket);

            //Сохраняем значение, которое находится между фигурными скобками
            String subString = string.substring(indexOfOpeningBracket + 1, indexOfClosingBracket);

            //Находим число, которое идет до открывающей фигурной скобки
            StringBuilder number = new StringBuilder();
            for (int i = indexOfOpeningBracket - 1; i >= 0; i--) {
                if (Character.isDigit(chars[i])) number.reverse().append(chars[i]);
                else break;
            }
            //System.out.println("Number - " + number.reverse().toString());

            //Удлинение строки между [], путем ее сложения number раз
            StringBuilder addString = new StringBuilder();
            for (int i = 0; i < Integer.parseInt(number.toString()); i++) {
                addString.append(subString);
            }
            //System.out.println("added - " + addString.toString());

            //Удаление числа + подстроки [], учитывая '[' и ']'
            newString.delete(indexOfOpeningBracket - number.length(), indexOfClosingBracket + 1);
            //System.out.println("deleted - " + newString.toString());

            //Вставление полученной увеличенной строки
            newString.insert(indexOfOpeningBracket - number.length(), addString);
            //System.out.println("Inserted - " + newString.toString());

            //Вызов программы еще раз, до тех пор пока число открывающих фигурных скобок не будет равно 0
            return start2(newString.toString());
        } else return string;
    }


    //Первая попытка написания программы, слишком долгое выполнение
    public static String start1(String string) {

        //Разбиваю строку на Лист, где каждый элемент - это символ строки
        LinkedList<Character> list1 = new LinkedList<>();
        for (Character x : string.toCharArray()) {
            list1.add(x);
        }

        //Нахожу все символы, которые идут до числа
        LinkedList<Character> list2 = new LinkedList<>(list1);
        StringBuilder lettersUntilNumber = new StringBuilder();
        for (Character x : list1) {
            if (!Character.isDigit(x)) {
                lettersUntilNumber.append(x);
                list2.removeFirst();
            } else break;
        }

        if (list2.size() == 0) return lettersUntilNumber.toString();


        //Нахожу само число, идущее до '['
        list1 = new LinkedList<>(list2);
        StringBuilder number = new StringBuilder();
        for (Character x : list1) {
            list2.removeFirst();
            if (x.equals('[')) {
                break;
            } else {
                number.append(x);
            }
        }

        //Нахожу все то, что находится в [], учитывая рекурсию
        list1 = new LinkedList<>(list2);
        StringBuilder lettersInBrackets = new StringBuilder();
        int count = 1;
        for (Character x : list1) {
            list2.removeFirst();
            if (x.equals('[')) count++;
            else if (x.equals(']')) count--;

            if (count == 0) break;
            else lettersInBrackets.append(x);
        }

        //Нахожу строку, которая находится в []
        StringBuilder answerInBrackets = new StringBuilder();
        for (int i = 0; i < Integer.parseInt(number.toString()); i++) {
            answerInBrackets.append(start1(lettersInBrackets.toString()));
        }

        //Сохраняю все то, что идет после
        StringBuilder lastLetters = new StringBuilder();
        for (Character x : list2) {
            lastLetters.append(x);
        }

        //Получаю строку, из остатка
        String wordsUntilTheEnd = "";
        if (list2.size() > 0) {
            wordsUntilTheEnd = start1(lastLetters.toString());
        }

        return lettersUntilNumber.toString() + answerInBrackets.toString() + wordsUntilTheEnd;
    }
}