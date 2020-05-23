package chapter14;

import tool.Page;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(urlPatterns={"/chapter14/transaction"})
public class Transaction extends HttpServlet {
  public void doPost (
    HttpServletRequest request, HttpServletResponse response
  ) throws ServletException, IOException {

    // 出力ストリームを取得
    PrintWriter out = response.getWriter();

    Page.header(out);

    try {

      // コンストラクタを使ってInitialContextオブジェクトを取得
      // JNDIを使うための窓口になる
      // コネクションプールからコネクションを取得
      InitialContext ic = new InitialContext();

      // データソースの取得
      // 因数にはJDNIのリソース名を指定する
      // context.xmlのResource要素のname属性を参照
      DataSource ds =(DataSource)ic.lookup("java:/comp/env/jdbc/book");


      // データベースへの接続を確立
      Connection con = ds.getConnection();

      // リクエストパラメータ各種を取得
      int id = Integer.parseInt(request.getParameter("id"));
      String name = request.getParameter("name");
      String gender = request.getParameter("gender");

      // オートコミットをオフにする
      con.setAutoCommit(false);

      // データの追加処理のSQL文を準備
      PreparedStatement st = con.prepareStatement(
      "insert into temperature_members values(?, ?, ?, null)");

      // プレースホルダーにリクエストパラメータを代入
      // SQLインジェクションに対応
      st.setInt(1, id);
      st.setString(2, name);
      st.setString(3, gender);

      // データの追加処理を実行
      st.executeUpdate();

      // データの検索処理のSQL文を準備
      // 追加したidと同じidを持つ行をテーブルから検索
      st = con.prepareStatement(
        "select * from temperature_members where id=?");

      // プレースホルダーにidを代入
      st.setInt(1, id);

      // データの検索処理を実行
      ResultSet rs = st.executeQuery();

      // クエリの結果に該当する何行含まれているかを調べる
      // lineの初期値は0にしておく
      int line = 0;
      while (rs.next()) {
        line ++;
      }

      // 検索結果が１の場合
      // 新規登録されたIDのみが検索されているのでコミットできる
      // それ以外の場合
      // データ不整合によりロールバック処理に移る
      if (line == 1) {

        con.commit();
        out.println("会員を登録しました");

      } else {

        con.rollback();
        out.println("会員はすでに登録されています");

      }

      // オートコミットをオンに戻しておく
      con.setAutoCommit(true);


      st.close();
      con.close();

    } catch (Exception e) {
      e.printStackTrace(out);
    }

    Page.footer(out);

  }

}
