import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProfileConfValue } from 'app/shared/model/profile-conf-value.model';

type EntityResponseType = HttpResponse<IProfileConfValue>;
type EntityArrayResponseType = HttpResponse<IProfileConfValue[]>;

@Injectable({ providedIn: 'root' })
export class ProfileConfValueService {
    public resourceUrl = SERVER_API_URL + 'api/profile-conf-values';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/profile-conf-values';

    constructor(protected http: HttpClient) {}

    create(profileConfValue: IProfileConfValue): Observable<EntityResponseType> {
        return this.http.post<IProfileConfValue>(this.resourceUrl, profileConfValue, { observe: 'response' });
    }

    update(profileConfValue: IProfileConfValue): Observable<EntityResponseType> {
        return this.http.put<IProfileConfValue>(this.resourceUrl, profileConfValue, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IProfileConfValue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProfileConfValue[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProfileConfValue[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
