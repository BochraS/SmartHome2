import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISmartObject } from 'app/shared/model/smart-object.model';

type EntityResponseType = HttpResponse<ISmartObject>;
type EntityArrayResponseType = HttpResponse<ISmartObject[]>;

@Injectable({ providedIn: 'root' })
export class SmartObjectService {
    public resourceUrl = SERVER_API_URL + 'api/smart-objects';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/smart-objects';

    constructor(protected http: HttpClient) {}

    create(smartObject: ISmartObject): Observable<EntityResponseType> {
        return this.http.post<ISmartObject>(this.resourceUrl, smartObject, { observe: 'response' });
    }

    update(smartObject: ISmartObject): Observable<EntityResponseType> {
        return this.http.put<ISmartObject>(this.resourceUrl, smartObject, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISmartObject>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISmartObject[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISmartObject[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
