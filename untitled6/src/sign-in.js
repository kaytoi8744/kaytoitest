import { Auth } from "aws-amplify";
import { initAuth } from "./auth";
import { App, URLOpenListenerEvent } from '@capacitor/app';

class App {
    constructor() {
        initAuth();
        this.attachEventlistener();
    }

    /** UIにイベントをアタッチする */
    attachEventlistener() {
        document
            .getElementById("sign-in")
            .addEventListener("submit", async (event) => {
                event.preventDefault();
                const email = document.getElementById("email").value;
                const password = document.getElementById("password").value;
                if (!email || !password) {
                    alert("Please input email and password.");
                    return;
                }
                await this.signIn(email, password);
            });
    }

    /** サインインを実行する */
    async signIn(email, password) {
        try {
            const result = await Auth.signIn(email, password);
            console.log(result);
            document.getElementById("id-token").value =
                result.signInUserSession.idToken.jwtToken;
        } catch (error) {
            console.log(error);
            alert(error.message);
        }
    }
}

new App();