import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ConfValue } from 'app/shared/model/conf-value.model';
import { ConfValueService } from './conf-value.service';
import { ConfValueComponent } from './conf-value.component';
import { ConfValueDetailComponent } from './conf-value-detail.component';
import { ConfValueUpdateComponent } from './conf-value-update.component';
import { ConfValueDeletePopupComponent } from './conf-value-delete-dialog.component';
import { IConfValue } from 'app/shared/model/conf-value.model';

@Injectable({ providedIn: 'root' })
export class ConfValueResolve implements Resolve<IConfValue> {
    constructor(private service: ConfValueService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ConfValue> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<ConfValue>) => response.ok),
                map((confValue: HttpResponse<ConfValue>) => confValue.body)
            );
        }
        return of(new ConfValue());
    }
}

export const confValueRoute: Routes = [
    {
        path: 'conf-value',
        component: ConfValueComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ConfValues'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'conf-value/:id/view',
        component: ConfValueDetailComponent,
        resolve: {
            confValue: ConfValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ConfValues'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'conf-value/new',
        component: ConfValueUpdateComponent,
        resolve: {
            confValue: ConfValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ConfValues'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'conf-value/:id/edit',
        component: ConfValueUpdateComponent,
        resolve: {
            confValue: ConfValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ConfValues'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const confValuePopupRoute: Routes = [
    {
        path: 'conf-value/:id/delete',
        component: ConfValueDeletePopupComponent,
        resolve: {
            confValue: ConfValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ConfValues'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
