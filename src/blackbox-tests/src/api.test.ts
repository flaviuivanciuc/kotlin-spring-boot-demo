import axios from "axios"
import {getKeycloakAccessToken} from "./get-token";

const BASE_URL = "http://localhost:8080"

describe("API Blackbox Tests", () => {
    it("should retrieve all customers", async () => {
        const token = await getKeycloakAccessToken("test_flaviu", "test_flaviu")
        let response

        try {
            response = await axios.get(`${BASE_URL}/customers`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })

        } catch (err) {
            console.log(err)
        }

        expect(response?.status).toBe(200)
    })

    it("should add a customer", async () => {
        const token = await getKeycloakAccessToken("test_flaviu", "test_flaviu")
        let response = undefined

        try {
            const payload = {firstName: "John", lastName: "Doe"}
            response = await axios.post(`${BASE_URL}/customers`, payload,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            )
        } catch (err) {
            console.log(err)
        }

        expect(response?.status).toBe(201)
    })

    it("should delete a customer", async () => {
        const token = await getKeycloakAccessToken("test_flaviu", "test_flaviu")
        let response = undefined

        try {
            const RECORD_ID = "1"
            response = await axios.delete(`${BASE_URL}/customers/${RECORD_ID}`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            )
        } catch (err) {
            console.log(err)
        }

        expect(response?.status).toBe(204)
    })
})