package DataVerificationService;

import unisoft.ws.*;
import unisoft.ws.fnsndscaws2.request.NdsRequest2;
import unisoft.ws.fnsndscaws2.response.NdsResponse2;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws UnsupportedEncodingException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String date = dateFormat.format(new Date());
        String INN = ""; //5043014134 for example
        String KPP = ""; // 504301001
        String cp1251 = "cp1251";
        Scanner scanner = new Scanner(System.in);

        System.out.print(new String("Введите ИНН: ".getBytes(cp1251)));
        INN = scanner.nextLine();

        System.out.print(new String("Введите КПП или нажмите \"Enter\" для пропуска этого шага : ".getBytes(cp1251)));
        KPP = scanner.nextLine();

        scanner.close();

        try {
            FNSNDSCAWS2 fnsndscaws2 = new FNSNDSCAWS2();
            FNSNDSCAWS2Port fnsndscaws2Port = fnsndscaws2.getFNSNDSCAWS2Port();

            NdsRequest2.NP np = new NdsRequest2.NP();
            np.setINN(INN);
            np.setKPP(KPP);
            np.setDT(date);

            NdsRequest2 npIn = new NdsRequest2();
            npIn.getNP().add(np);

            NdsResponse2 res = fnsndscaws2Port.ndsRequest2(npIn);

             for (NdsResponse2.NP nnn : res.getNP()){
                int status = Integer.parseInt(nnn.getState());
                String answer = "";
                switch(status){
                    case 0:
                        answer = "Налогоплательщик зарегистрирован в ЕГРН и имел статус действующего в указанную дату"; break;
                    case 1:
                        answer = "Налогоплательщик зарегистрирован в ЕГРН, но не имел статус действующего в указанную дату"; break;
                    case 2:
                        answer = "Налогоплательщик зарегистрирован в ЕГРН"; break;
                    case 3:
                        answer = "Налогоплательщик с указанным ИНН зарегистрирован в ЕГРН, КПП не соответствует ИНН или не указан"; break;
                    case 4:
                        answer = "Налогоплательщик с указанным ИНН не зарегистрирован в ЕГРН"; break;
                    case 5:
                        answer = "Некорректный ИНН"; break;
                    case 6:
                        answer = "Недопустимое количество символов ИНН"; break;
                    case 7:
                        answer = "Недопустимое количество символов КПП"; break;
                    case 8:
                        answer = "Недопустимые символы в ИНН"; break;
                    case 9:
                        answer = "Недопустимые символы в КПП"; break;
                    case 10:
                        answer = "КПП не должен использоваться при проверке ИП"; break;
                    case 11:
                        answer = "некорректный формат даты"; break;
                    case 12:
                        answer = "некорректная дата (ранее 01.01.1991 или позднее текущей даты)"; break;
                }
                 System.out.println(new String(answer.getBytes(cp1251)));
             }

        }catch (Exception e){
            System.out.println("error");
        }
    }
}
