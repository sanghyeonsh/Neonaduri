import {apiInstance} from '.'

const api = apiInstance();

async function getSat(success,fail){
    await api.get('/api/statistics/sat').then(success).catch(fail)
}

async function getVisited(success,fail){
    await api.get('/api/statistics/visited').then(success).catch(fail)
}

async function getSel(success,fail){
    await api.get('/api/statistics/sel').then(success).catch(fail)
}


export {getSat , getVisited, getSel}
