package com.oamorales.myresume.utils;

import android.app.Activity;
import android.widget.Toast;

import com.oamorales.myresume.R;
import com.oamorales.myresume.models.Degree;
import com.oamorales.myresume.models.Language;
import com.oamorales.myresume.models.PersonInfo;
import com.oamorales.myresume.models.WorkExp;
import com.oamorales.myresume.repositories.MainActRepository;

import io.realm.Realm;

public class HtmlToPdf {

    public static String getHtml(Activity activity, Realm realm,  String path){
        /**Person info*/
        PersonInfo personInfo = MainActRepository.getPersonInfo(realm);
        String name = "";
        String email = "";
        String phone = "";
        if (personInfo!=null){
            name = personInfo.getName();
            email = personInfo.getEmail();
            phone = personInfo.getPhone();
        }else{
            Toast.makeText(activity, activity.getString(R.string.person_info_missing), Toast.LENGTH_SHORT).show();
            return null;
        }

        /**Last degree info*/
        Degree degree = MainActRepository.getDegree(realm);
        String lastDegree = "";
        String university = "";
        if (degree != null){
            lastDegree = degree.getDegreeTittle() + ", "+ degree.getStartDate()+" - "+degree.getEndDate();
            university = degree.getUniversity();
        }else {
            Toast.makeText(activity, activity.getString(R.string.degree_missing), Toast.LENGTH_SHORT).show();
            return null;
        }
        /**Working experience info*/
        WorkExp workExp = MainActRepository.getWorkExp(realm);
        String lastWorkExp = "";
        String company = "";
        String actualPosition = "";
        if (workExp != null){
            lastWorkExp = workExp.getPosition() + ", "+ workExp.getStartDate()+" - " + workExp.getEndDate();
            company = workExp.getCompanyName();
            actualPosition = workExp.getPosition();
        }else {
            lastWorkExp = activity.getString(R.string.work_exp_missing);
            actualPosition = degree.getDegreeTittle();
        }
        /**Language info*/
        Language language = MainActRepository.getLanguage(realm);
        String languageName = "";
        String languageLevel = "";
        if (language == null){
            Toast.makeText(activity, activity.getString(R.string.language_missing), Toast.LENGTH_LONG).show();
            return null;
        }else{
            languageName = language.getLanguage();
            switch (language.getLevel()){
                case 1:
                    languageLevel = activity.getString(Levels.LANGUAGE_LEVEL_1);
                    break;
                case 2:
                    languageLevel = activity.getString(Levels.LANGUAGE_LEVEL_2);
                    break;
                case 3:
                    languageLevel = activity.getString(Levels.LANGUAGE_LEVEL_3);
                    break;
                case 4:
                    languageLevel = activity.getString(Levels.LANGUAGE_LEVEL_4);
                    break;
                case 5:
                    languageLevel = activity.getString(Levels.LANGUAGE_LEVEL_5);
            }
            languageName = languageName + " - " + languageLevel;
        }

        String degrees = activity.getString(R.string.degrees);
        String work_exps = activity.getString(R.string.working_experience);
        String languages = activity.getString(R.string.languages);
        return " <html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "<title>Documento sin título</title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<table width=\"600\" border=\"0\">\n" +
                "  <tr>\n" +
                "    <td width=\"184\"><div align=\"center\"><img src=\""+path+"\" alt=\"\" name=\"person_image\" width=\"142\" height=\"157\" id=\"person_image\"/></div>" +
                "    </td>\n" +
                "    <td width=\"410\" align=\"center\">"+
                "     <p style=\"color:blue;\"><strong><h2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+ name +"</h2></strong></p>\n" +
                "     <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+ actualPosition +" </p>"+
                "     <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+ email +"</p>" +
                "     <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+ phone +"</p>" +
                "    </td>\n" +
                "  </tr>\n" +
                "</table>\n" +
                //"<hr width=\"790\" />" +
                "<hr style=\"height:6px;border-width:0;color:blue;background-color:gray\" />"+
                "       <p><strong> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + degrees +"</strong></p>" +
                "<p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                lastDegree + "</p>\n" +
                "<p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                university + "</p>\n" +
                "<hr width=\"630\" style=\"color:blue;\" />\n" +
                //"<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>" +
                "      <p><strong> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + work_exps + "</strong></p>\n" +
                "<p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                //"   Ingeniero de Software, Ago 2018 - Abr 2019</p>\n" +
                lastWorkExp + "</p>\n" +
                "<p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                //"   Bancaribe</p>\n" +
                company + "</p>\n" +
                "<hr width=\"630\" style=\"color:blue;\"/>\n" +
                "<p>&nbsp;</p>\n" +
                "      <p><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + languages +"</strong></p>\n" +
                "<p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                languageName +"</p>\n" +
                //"<p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                //" Inglés - Profesional completo</p>\n" +
                "<p>&nbsp;</p>\n" +
                //"<hr width=\"790\" />\n" +
                "<hr style=\"height:6px;border-width:0;color:blue;background-color:gray\" />"+
                "</body></html>";
    }
}
