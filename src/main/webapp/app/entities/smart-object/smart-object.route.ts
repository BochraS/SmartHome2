import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SmartObject } from 'app/shared/model/smart-object.model';
import { SmartObjectService } from './smart-object.service';
import { SmartObjectComponent } from './smart-object.component';
import { SmartObjectDetailComponent } from './smart-object-detail.component';
import { SmartObjectUpdateComponent } from './smart-object-update.component';
import { SmartObjectDeletePopupComponent } from './smart-object-delete-dialog.component';
import { ISmartObject } from 'app/shared/model/smart-object.model';

@Injectable({ providedIn: 'root' })
export class SmartObjectResolve implements Resolve<ISmartObject> {
    constructor(private service: SmartObjectService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<SmartObject> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SmartObject>) => response.ok),
                map((smartObject: HttpResponse<SmartObject>) => smartObject.body)
            );
        }
        return of(new SmartObject());
    }
}

export const smartObjectRoute: Routes = [
    {
        path: 'smart-object',
        component: SmartObjectComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmartObjects'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'smart-object/:id/view',
        component: SmartObjectDetailComponent,
        resolve: {
            smartObject: SmartObjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmartObjects'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'smart-object/new',
        component: SmartObjectUpdateComponent,
        resolve: {
            smartObject: SmartObjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmartObjects'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'smart-object/:id/edit',
        component: SmartObjectUpdateComponent,
        resolve: {
            smartObject: SmartObjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmartObjects'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const smartObjectPopupRoute: Routes = [
    {
        path: 'smart-object/:id/delete',
        component: SmartObjectDeletePopupComponent,
        resolve: {
            smartObject: SmartObjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmartObjects'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
