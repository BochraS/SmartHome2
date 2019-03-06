import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IConfValue } from 'app/shared/model/conf-value.model';

type EntityResponseType = HttpResponse<IConfValue>;
type EntityArrayResponseType = HttpResponse<IConfValue[]>;

@Injectable({ providedIn: 'root' })
export class ConfValueService {
    public resourceUrl = SERVER_API_URL + 'api/conf-values';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/conf-values';

    constructor(protected http: HttpClient) {}

    create(confValue: IConfValue): Observable<EntityResponseType> {
        return this.http.post<IConfValue>(this.resourceUrl, confValue, { observe: 'response' });
    }

    update(confValue: IConfValue): Observable<EntityResponseType> {
        return this.http.put<IConfValue>(this.resourceUrl, confValue, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IConfValue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IConfValue[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IConfValue[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
