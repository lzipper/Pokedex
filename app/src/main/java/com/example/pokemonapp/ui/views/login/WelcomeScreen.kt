package com.example.pokemonapp.ui.views.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pokemonapp.R

@Composable
fun WelcomeScreen(
    onSignInSignUp: (email: String) -> Unit,
    onSignInAsGuest: () -> Unit,
) {
    SignInCreateAccount(
        onSignInSignUp = onSignInSignUp,
        onSignInAsGuest = onSignInAsGuest,
        //onFocusChange = { focused -> showBranding = !focused },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    )
}

@Composable
private fun SignInCreateAccount(
    onSignInSignUp: (email: String) -> Unit,
    onSignInAsGuest: () -> Unit,
    //onFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    //val emailState by rememberSaveable(stateSaver = EmailStateSaver) {
    //    mutableStateOf(EmailState())
    //}

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 64.dp, bottom = 12.dp)
        )
        val onSubmit = {
            //if (emailState.isValid) {
            //    onSignInSignUp(emailState.text)
            //} else {
            //    emailState.enableShowErrors()
            //}
        }
        //onFocusChange(emailState.isFocused)
        //Email(emailState = emailState, imeAction = ImeAction.Done, onImeAction = onSubmit)
        TextField(value = "EMAIL", onValueChange = {})
        Button(
            onClick = { onSignInSignUp("test@email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, bottom = 3.dp)
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleSmall
            )
        }
        OrSignInAsGuest(
            onSignInAsGuest = onSignInAsGuest,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun OrSignInAsGuest(
    onSignInAsGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.paddingFromBaseline(top = 25.dp)
        )
        OutlinedButton(
            onClick = onSignInAsGuest,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 24.dp),
        ) {
            Text(text = stringResource(id = R.string.app_name))
        }
    }
}