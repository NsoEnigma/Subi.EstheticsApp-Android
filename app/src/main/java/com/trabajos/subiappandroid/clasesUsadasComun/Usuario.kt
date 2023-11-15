package com.trabajos.subiappandroid.clasesUsadasComun

/**
 * Clase Usuario que representa un usuario en la aplicación.
 * Esta clase implementa la interfaz Parcelable para poder ser transferida entre componentes de Android.
 * @author Alejandro Asencio Montes
 */

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate

class Usuario : Parcelable {

    var nombre:String?
    var apellidos:String?
    var contraseña:String
    var telefono:String?
    lateinit var email:String

    /**
     * Constructor vacío que crea un objeto Usuario sin valores por defecto.
     * @author Alejandro Asencio Montes
     */
    constructor(parcel: Parcel) : this() {
        nombre = parcel.readString()
        apellidos = parcel.readString()
        contraseña = parcel.readString()!!
        telefono = parcel.readString()
        email = parcel.readString()!!
    }

    /**
     * Constructor que crea un objeto Usuario con los valores proporcionados.
     *
     * @param nombre El nombre del usuario.
     * @param pass La contraseña del usuario.
     * @param email El email del usuario.
     * @param fn La fecha de nacimiento del usuario.
     * @author Alejandro Asencio Montes
     */

    constructor(nombre: String,apellidos: String,pass: String,telefono: String,email: String) : this() {
        this.nombre = nombre
        this.apellidos = apellidos
        this.contraseña = pass
        this.telefono = telefono
        this.email = email
    }

    constructor(nombre: String,apellidos: String,email: String,telefono: String) : this() {
        this.nombre = nombre
        this.apellidos = apellidos
        this.telefono = telefono
        this.email = email
    }

    /**
     * Constructor Parcelable que crea un objeto Usuario a partir de los datos contenidos en un objeto Parcel.
     *
     * @param parcel Un objeto Parcel que contiene los datos del usuario.
     *
     * @throws RuntimeException Si se intenta leer un valor que no ha sido inicializado.
     * @author Alejandro Asencio Montes
     */

    constructor(){

        this.nombre=""
        this.apellidos=""
        this.telefono=""
        this.contraseña=""
    }

    /**
     * Escribe los valores del objeto Usuario en un objeto Parcel.
     *
     * @param parcel Un objeto Parcel en el que se escribirán los datos.
     * @param flags Un conjunto de flags opcionales.
     * @author Alejandro Asencio Montes
     */

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(apellidos)
        parcel.writeString(contraseña)
        parcel.writeString(telefono)
        parcel.writeString(email)
    }

    /**
     * Describe el contenido del objeto Usuario.
     *
     * @return 0 siempre.
     * @author Alejandro Asencio Montes
     */
    override fun describeContents(): Int {
        return 0
    }

    /**
     * Creador Parcelable que permite la creación de un objeto Usuario a partir de un objeto Parcel.
     * @author Alejandro Asencio Montes
     */
    companion object CREATOR : Parcelable.Creator<Usuario> {

        override fun createFromParcel(parcel: Parcel): Usuario {
            return Usuario(parcel)
        }

        override fun newArray(size: Int): Array<Usuario?> {
            return arrayOfNulls(size)
        }
    }
}