import { Amplify } from "aws-amplify";

export const initAuth = () => {
    Amplify.configure({
        aws_cognito_region: "ap-northeast-1", // (required) - Region where Amazon Cognito project was created
        aws_user_pools_id: "ap-northeast-1_QurhY3lQD", // (optional) -  Amazon Cognito User Pool ID
        aws_user_pools_web_client_id: "4tc97qg3gojh7j8dhq2edje3vj", // (optional) - Amazon Cognito App Client ID (App client secret needs to be disabled)
    });
};