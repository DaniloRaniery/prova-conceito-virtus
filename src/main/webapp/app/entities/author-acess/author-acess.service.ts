import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { AuthorAcess } from './author-acess.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<AuthorAcess>;

@Injectable()
export class AuthorAcessService {

    private resourceUrl =  SERVER_API_URL + 'api/author-acesses';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/author-acesses';

    constructor(private http: HttpClient) { }

    create(authorAcess: AuthorAcess): Observable<EntityResponseType> {
        const copy = this.convert(authorAcess);
        return this.http.post<AuthorAcess>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(authorAcess: AuthorAcess): Observable<EntityResponseType> {
        const copy = this.convert(authorAcess);
        return this.http.put<AuthorAcess>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<AuthorAcess>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<AuthorAcess[]>> {
        const options = createRequestOption(req);
        return this.http.get<AuthorAcess[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<AuthorAcess[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<AuthorAcess[]>> {
        const options = createRequestOption(req);
        return this.http.get<AuthorAcess[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<AuthorAcess[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: AuthorAcess = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<AuthorAcess[]>): HttpResponse<AuthorAcess[]> {
        const jsonResponse: AuthorAcess[] = res.body;
        const body: AuthorAcess[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to AuthorAcess.
     */
    private convertItemFromServer(authorAcess: AuthorAcess): AuthorAcess {
        const copy: AuthorAcess = Object.assign({}, authorAcess);
        return copy;
    }

    /**
     * Convert a AuthorAcess to a JSON which can be sent to the server.
     */
    private convert(authorAcess: AuthorAcess): AuthorAcess {
        const copy: AuthorAcess = Object.assign({}, authorAcess);
        return copy;
    }
}
