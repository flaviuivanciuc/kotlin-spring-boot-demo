import axios from "axios"
import qs from "querystring"

const KEYCLOAK_URL = "http://localhost:7000/realms/reeserve/protocol/openid-connect/token"
const CLIENT_ID = "reeserve"

export async function getKeycloakAccessToken(username: string, password: string) {
    const payload = {
        client_id: CLIENT_ID,
        username,
        password,
        grant_type: "password"
    }

    let response = undefined
    try {
        response = await axios.post(KEYCLOAK_URL, qs.stringify(payload), {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        })

        return response.data.access_token
    } catch (err) {
        console.log(err)
    }

    return response
}
