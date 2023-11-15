package com.trabajos.subiappandroid.clasesGenerales

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trabajos.subiappandroid.clasesUsadasComun.Usuario

/**
 * Clase madre para las actividades del proyecto SubiEsthetics.
 * Contiene propiedades y métodos generales que son heredados por las actividades hijas.
 * @constructor Crea una instancia de la clase ActividadMadre.
 * @author Alejandro Asencio Montes
 */

open abstract class ActividadMadre() : AppCompatActivity() {

    var usuarioLogado: Usuario? = null


    /**
     * Cambia a la pantalla especificada por el nombre de la actividad.
     * @param nombreActividad El nombre de la actividad a la que se desea cambiar.
     * @author Alejandro Asencio Montes
     */

    public fun cambiarAPantalla(nombreActividad: String): Unit {
        val intent: Intent = Intent(
            this,
            Class.forName("com.trabajos.subiappandroid." + nombreActividad)
        )
        startActivity(intent)
    }
}