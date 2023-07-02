package com.disfluency.screens.patient;

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.disfluency.R
import com.disfluency.components.inputs.*
import com.disfluency.data.PatientRepository
import com.disfluency.model.Patient
import com.disfluency.navigation.Route
import java.time.LocalDate

@Composable
fun FormNewPatient(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 10.dp)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val NEXT_INPUT_ON_ENTER = KeyboardOptions(imeAction = ImeAction.Next)
        val NEXT_AND_CAPITALIZE_WORDS =
            NEXT_INPUT_ON_ENTER.copy(capitalization = KeyboardCapitalization.Words)

        val profilePic = remember { mutableStateOf(R.drawable.avatar_null) }
        ImagePicker(profilePic)
//        var selectedImage by remember { mutableStateOf() }

        val patientName = inputAsString("Nombre", keyboardOptions = NEXT_AND_CAPITALIZE_WORDS)
        val patientLastname = inputAsString(label = "Apellido", keyboardOptions = NEXT_AND_CAPITALIZE_WORDS)
        val patientDNI = inputAsString(
            "DNI",
            validations = listOf { it.isDigitsOnly() },
            keyboardOptions = NEXT_INPUT_ON_ENTER.copy(keyboardType = KeyboardType.NumberPassword)
        )

        val email = inputAsString(label = "Correo Electrónico", keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email, imeAction = ImeAction.Done
        ), validations = listOf { Patterns.EMAIL_ADDRESS.asPredicate().test(it) })

        val todaysDate = LocalDate.now()
        val patientBirthDate = inputDate("Fecha de Nacimiento", maxDate = todaysDate)

        //TODO: Validar
        val weeklyTurn = remember{ mutableStateOf("") }
        DummyDaysOfWeekCheckbox(label="Día/s Asignado/s", state = weeklyTurn)
        
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Button(
                onClick = {
                    val attributes =
                        listOf(patientName, patientLastname, patientDNI, email, patientBirthDate)
                    attributes.forEach { it.validate() }
                    if (attributes.all { !it.wrongValue() }) {
                        val patient = Patient(
                            name = patientName.value,
                            lastName = patientLastname.value,
                            id = patientDNI.value.toInt(),
                            dateOfBirth = patientBirthDate.value!!,
                            /* Este value es nullable porque la fecha hasta que se elija va a ser null.
                             * Al agregar !! "casteo" de nullable a no nullable (si fuera null, romperia).
                             * En el validate se valida que no sea null.
                             */
                            email = email.value,
                            joinedSince = todaysDate,
                            weeklyHour = "18:00", //TODO
                            weeklyTurn = weeklyTurn.value,
                            profilePic = profilePic.value
                        )
                        PatientRepository.addPatient(patient)
                        navController.navigate(Route.Paciente.routeTo(patient.id))
                    }
                }
            ) {
                Text("Crear")
            }
            Button(
                colors =  ButtonDefaults.buttonColors(
                    contentColorFor(MaterialTheme.colorScheme.inversePrimary)
                ),
                onClick = {navController.navigate(Route.Pacientes.route)}
            ) {
                Text(text = "Cancelar")
            }
        }

    }
}


