package com.trabajos.subiappandroid.pantallas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.trabajos.subiappandroid.R
import com.trabajos.subiappandroid.clasesGenerales.ActividadMadre
import com.trabajos.subiappandroid.clasesUsadasComun.Usuario
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class RegisterActivity : ActividadMadre() {


    val botonRegistrarse: Button by lazy { this.findViewById<Button>(R.id.botonRegistrar) }
    val emailIntroducir: EditText by lazy { this.findViewById<EditText>(R.id.emailIntroducir) }
    val usuarioIntroducir: EditText by lazy { this.findViewById<EditText>(R.id.nombreIntroducir) }
    val apellidosIntroducir: EditText by lazy { this.findViewById<EditText>(R.id.apellidosIntroducir) }
    val editTextTextPassword: EditText by lazy { this.findViewById<EditText>(R.id.contraseñaIntroducir) }
    val telefonoIntroducir: EditText by lazy { this.findViewById<EditText>(R.id.telefonoIntroducir) }

    private val db = FirebaseFirestore.getInstance()

    /**
     * Configura el contenido de la vista y agrega un listener
     * al botón de registro que verifica que los campos requeridos no estén vacíos y crea un usuario en Firebase Auth y
     * Firestore si se cumplen todas las condiciones.
     *
     * @param savedInstanceState Objeto Bundle que contiene el estado anteriormente guardado de la actividad en caso de que
     * se haya destruido y vuelto a crear.
     * @author Alejandro Asencio Montes
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val textView = findViewById<TextView>(R.id.textoIniciarSesion)
        val text = getString(R.string.iniciarSesionAhora)
        val spannableString = SpannableString(text)

        // Crear un ClickableSpan para la palabra "Regístrate"
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Abre la nueva actividad aquí
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Configurar el rango de texto que será cliclable
        val startIndex = text.indexOf("Iniciar Sesión")
        val endIndex = startIndex + "Iniciar Sesión".length
        spannableString.setSpan(clickableSpan, startIndex, endIndex, 0)

        // Aplicar el SpannableString al TextView
        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()

        // Establecer el tipo de entrada para texto en minúsculas
        emailIntroducir.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

        // Agregar un InputFilter para convertir texto a minúsculas
        val inputFilter = InputFilter { source, _, _, _, _, _ ->
            source.toString().toLowerCase(Locale.getDefault())
        }

        emailIntroducir.filters = arrayOf(inputFilter)

        botonRegistrarse.setOnClickListener {
            var camposVacios: Boolean = false;
            if (usuarioIntroducir.text.toString().isBlank()) {
                Toast.makeText(this, R.string.rellenarUsuario, Toast.LENGTH_SHORT).show()
                camposVacios = true;
            }
            if (apellidosIntroducir.text.toString().isBlank()) {
                Toast.makeText(this, R.string.rellenarApellidos, Toast.LENGTH_SHORT).show()
                camposVacios = true;
            }
            if (emailIntroducir.text.toString().isBlank()) {
                Toast.makeText(this, R.string.rellenarEmail, Toast.LENGTH_SHORT).show()
                camposVacios = true;
            }
            if (telefonoIntroducir.text.toString().isBlank()) {
                Toast.makeText(this, R.string.rellenarTelefono, Toast.LENGTH_SHORT).show()
                camposVacios = true;
            }
            if (editTextTextPassword.text.toString().isBlank()) {
                Toast.makeText(this, R.string.rellenarPassword, Toast.LENGTH_SHORT).show()
                camposVacios = true;
            }
            if (!camposVacios)
            {

                val nombre: String = usuarioIntroducir.text.toString()
                val apellidos: String = apellidosIntroducir.text.toString()
                val email: String = emailIntroducir.text.toString()
                val telefono: String = telefonoIntroducir.text.toString()
                val contraseña: String = editTextTextPassword.text.toString()

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailIntroducir.text.toString(), editTextTextPassword.text.toString()).addOnCompleteListener{

                    if (it.isSuccessful)
                    {
                        usuarioLogado = Usuario(
                            nombre,
                            apellidos,
                            email,
                            telefono
                        )
                        db.collection("usuarios")
                            .document(emailIntroducir.text.toString()).set(
                                hashMapOf(
                                    "nombre" to usuarioLogado!!.nombre,
                                    "apellidos" to usuarioLogado!!.apellidos,
                                    "teléfono" to usuarioLogado!!.telefono,
                                    "email" to usuarioLogado!!.email,
                                )
                            )
                        val intent: Intent = Intent(this,
                            CitasActivity::class.java)
                        this.startActivity(intent)
                    } else{
                        showAlert()
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
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}