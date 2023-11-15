package com.trabajos.subiappandroid.pantallas

/**
 * La clase LoginActivity extiende de la clase ActividadMadre. Es la pantalla inicial que se muestra
 * al usuario cuando este abre la aplicacion. Permite al usuario iniciar sesion en su cuenta ya existente
 * o registrarse en la aplicacion si aun no posee una cuenta.
 * @property botonIniciarSesionInicio Botón para iniciar sesión
 * @property emailIntroducir Campo de texto para introducir el correo electrónico del usuario
 * @property contenidoPasswordLogin Campo de texto para introducir la contraseña del usuario
 * @author Alejandro Asencio Montes
 */

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.trabajos.subiappandroid.R
import com.trabajos.subiappandroid.clasesGenerales.ActividadMadre
import com.trabajos.subiappandroid.clasesUsadasComun.Usuario
import java.io.*
import java.nio.file.FileSystems
import java.nio.file.Files
import java.time.LocalDate

class LoginActivity : ActividadMadre() {

    val botonLoginInicio: Button by lazy { this.findViewById<Button>(R.id.botonLoginInicio) }
    val emailIntroducir: EditText by lazy { this.findViewById<EditText>(R.id.emailIntroducir) }
    val contenidoPasswordLogin: EditText by lazy { this.findViewById<EditText>(R.id.contraseñaIntroducir) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        val recordarContraseña: CheckBox = findViewById<CheckBox>(R.id.recordarDatos)
        val botonIniciarSesion: Button = findViewById<Button>(R.id.botonLoginInicio)

        val textView = findViewById<TextView>(R.id.textoRegistrar)
        val textRegister = getString(R.string.registrarmeAhora)
        val spannableString = SpannableString(textRegister)

        // Crear un ClickableSpan para la palabra "Regístrate"
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Abre la nueva actividad aquí, por ejemplo, SignUpActivity
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Configurar el rango de texto que será cliclable
        val startIndex = textRegister.indexOf("Regístrate")
        val endIndex = startIndex + "Regístrate".length
        spannableString.setSpan(clickableSpan, startIndex, endIndex, 0)

        // Aplicar el SpannableString al TextView
        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()

        /**
         * Con esta funcion conseguiremos que nuestro usuario pueda recordar sus datos para asi cada vez
         * que la aplicación sea abierta tenga los ultimos datos de usuario utilizados ya completos en sus
         * debidas casillas
         * @property recordarContraseña Opcion marcada del usuario para recordad datos
         * @author Alejandro Asencio Montes
         */

        try {
            val preferencias: SharedPreferences = getSharedPreferences(
                "preferenciasPersonalizadas", Context.MODE_PRIVATE
            )
            val tipoAlmacenamiento: String? = preferencias.getString(
                "tipoAlmacenamiento",
                resources.getString(R.string.almacenamientoInterno)
            )
            var lector: BufferedReader? = null
            if (tipoAlmacenamiento.equals(resources.getString(R.string.almacenamientoInterno))) {
                lector = BufferedReader(InputStreamReader(this.openFileInput("datosLogin.txt")))

            } else if (tipoAlmacenamiento.equals(resources.getString(R.string.almacenamientoExternoPublico))) {
                val file: File = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    "datosLogin.txt"
                )
                lector = BufferedReader(FileReader(file))
            } else {
                val file: File = File(getExternalFilesDir(null), "datosLogin.txt")
                lector = BufferedReader(FileReader(file))
            }
            if (lector != null) {
                emailIntroducir.setText(lector.readLine())
                contenidoPasswordLogin.setText(lector.readLine())
                recordarContraseña.isChecked = true
            }
        } catch (e: FileNotFoundException) {
            Log.d("Archivo datosLogin", "Archivo datosLogin no encontrado")
        }

        botonIniciarSesion.setOnClickListener {

            var camposVacios: Boolean = false;

            if (emailIntroducir.text.toString().isBlank()) {
                Toast.makeText(this, R.string.rellenarEmail, Toast.LENGTH_SHORT).show()
                camposVacios = true;
            }

            if (contenidoPasswordLogin.text.toString().isBlank()) {
                Toast.makeText(this, R.string.rellenarPassword, Toast.LENGTH_SHORT).show()
                camposVacios = true;
            }
            if (!camposVacios) {

                val preferencias: SharedPreferences = getSharedPreferences(
                    "preferenciasPersonalizadas", Context.MODE_PRIVATE
                )

                if (recordarContraseña.isChecked) {
                    var escritor: Writer
                    val tipoAlmacenamiento: String? =
                        preferencias.getString(
                            "tipoAlmacenamiento",
                            resources.getString(R.string.almacenamientoInterno)
                        )
                    if (tipoAlmacenamiento.equals(resources.getString(R.string.almacenamientoInterno))) {
                        escritor = OutputStreamWriter(
                            openFileOutput(
                                "datosLogin.txt",
                                Context.MODE_PRIVATE
                            )
                        )
                    } else if (tipoAlmacenamiento.equals(resources.getString(R.string.almacenamientoExternoPublico))) {
                        val archivo: File? = File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                            "datosLogin.txt"
                        )
                        if (Files.exists(
                                FileSystems.getDefault().getPath(archivo.toString())
                            ) == false
                        ) {
                            Files.createFile(FileSystems.getDefault().getPath(archivo.toString()))
                        }
                        escritor = FileWriter(archivo.toString())
                    } else { //Almacenamiento externo privado, por descarte
                        val archivo: File? = File(getExternalFilesDir(null), "datosLogin.txt");
                        if (Files.exists(
                                FileSystems.getDefault().getPath(archivo.toString())
                            ) == false
                        ) {
                            Files.createFile(FileSystems.getDefault().getPath(archivo.toString()))
                        }
                        escritor = FileWriter(archivo.toString())
                    }
                    escritor.write(emailIntroducir.text.toString() + "\n")
                    escritor.write(contenidoPasswordLogin.text.toString())
                    escritor.flush()
                    escritor.close()
                } else {
                    deleteFile("datosLogin.txt");

                }


                if (emailIntroducir.text.isNotEmpty() && contenidoPasswordLogin.text.isNotEmpty()) {

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                        emailIntroducir.text.toString(),
                        contenidoPasswordLogin.text.toString()
                    )
                        .addOnCompleteListener {

                            if (it.isSuccessful) {
                                FirebaseFirestore.getInstance().collection("usuarios")
                                    .whereEqualTo("email", emailIntroducir.text.toString()).get()
                                    .addOnSuccessListener { result ->
                                        for (document in result) {
                                            usuarioLogado = Usuario(
                                                "" + document.getString("nombre"),
                                                "" + document.getString("apellidos"),
                                                "" + document.getString("email"),
                                                LocalDate.now()
                                            )
                                            this.cambiarAPantalla("")
                                        }

                                    }
                            } else {
                                showAlert()
                            }
                        }

                }
            }
        }
    }

    /**
     * Función que muestra una alerta en caso de fallo al registrar al usuario en Firebase Auth
     */
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticado el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}