
const BASE_URL = import.meta.env.VITE_BASE_URL

export async function fetchGet<T>(relativeUrl: string) : Promise<T | undefined>{

    const url = `${BASE_URL}${relativeUrl}`

    const response = await fetch(url)
    if(!response.ok){
        throw new Error("Failed to fetch " + response.status + "  -  " + response.text)
    }

    const data: T = await response.json()
    return data || undefined;
}

export async function fetchPut<T>(relativeUrl: string, payload: any) : Promise<T | undefined>{
    const url = `${BASE_URL}${relativeUrl}`

    const response = await fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
    })
    if(!response.ok){
        throw new Error("Failed to fetch " + response.status + "  -  " + response.text)
    }

    const data: T = await response.json()
    return data || undefined;
    
}