package br.pro.israelsousa.gestaosalaobeleza.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.database.getStringOrNull
import br.pro.israelsousa.gestaosalaobeleza.model.Cliente

class DatabaseManager(context: Context, name: String?) : SQLiteOpenHelper(context,name,null,1)  {

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE CLIENTE(\n" +
                "\tID_CLIENTE VARCHAR(100) NOT NULL,\n" +
                "\tNOMECLIENTE VARCHAR(100),\n" +
                "\tTIPOCLIENTE VARCHAR(100),\n" +
                "\tUNIDADESALAO VARCHAR(100),\n" +
                "\tPRIMARY KEY (ID_CLIENTE)\n" +
                "\t);")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
       p0?.execSQL("DROP TABLE IF EXISTS CLIENTE")
        p0?.execSQL("CREATE TABLE CLIENTE(\n" +
                "\tID_CLIENTE VARCHAR(100) NOT NULL,\n" +
                "\tNOMECLIENTE VARCHAR(100),\n" +
                "\tTIPOCLIENTE VARCHAR(100),\n" +
                "\tUNIDADESALAO VARCHAR(100),\n" +
                "\tPRIMARY KEY (ID_CLIENTE)\n" +
                "\t);")
    }

    fun insereCliente(id: String, nome: String, tipo: String, unidade: String){
        var db = this.writableDatabase
//        //db?.execSQL("DROP TABLE CLIENTE")
//        db.execSQL("DROP TABLE CLIENTE")
//        db.inTransaction()
//        db.endTransaction()
        var cv = ContentValues()

        cv.put("ID_CLIENTE",id)
        cv.put("NOMECLIENTE",nome)
        cv.put("TIPOCLIENTE",tipo)
        cv.put("UNIDADESALAO",unidade)

        db.insert("CLIENTE","ID_CLIENTE",cv)


    }

    @SuppressLint("Range")
    fun listaCliente(): Cliente {

        val cursor: Cursor?
        var db = this.readableDatabase
        val newCliente = Cliente()

        cursor = db.rawQuery("select * from CLIENTE",null)

        if(cursor.moveToFirst()){
            do{
                         newCliente.nomeCliente = cursor.getString(cursor.getColumnIndex("NOMECLIENTE"))
                         newCliente.tipoCliente = cursor.getString(cursor.getColumnIndex("TIPOCLIENTE"))
                         newCliente.unidadeSalao = cursor.getString(cursor.getColumnIndex("UNIDADESALAO"))

            }while (cursor.moveToNext())
        }

        cursor.close();
        db.close()
        Log.d(
            "sucesso",
            "NOME DO USUÁRIO2 : ${newCliente.tipoCliente}")
        return newCliente
    }

    fun removeCliente(){
        var db = this.writableDatabase

        db.delete("CLIENTE", null,null)
    }
}