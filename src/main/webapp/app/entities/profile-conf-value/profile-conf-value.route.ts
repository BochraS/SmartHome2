import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ProfileConfValue } from 'app/shared/model/profile-conf-value.model';
import { ProfileConfValueService } from './profile-conf-value.service';
import { ProfileConfValueComponent } from './profile-conf-value.component';
import { ProfileConfValueDetailComponent } from './profile-conf-value-detail.component';
import { ProfileConfValueUpdateComponent } from './profile-conf-value-update.component';
import { ProfileConfValueDeletePopupComponent } from './profile-conf-value-delete-dialog.component';
import { IProfileConfValue } from 'app/shared/model/profile-conf-value.model';

@Injectable({ providedIn: 'root' })
export class ProfileConfValueResolve implements Resolve<IProfileConfValue> {
    constructor(private service: ProfileConfValueService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ProfileConfValue> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<ProfileConfValue>) => response.ok),
                map((profileConfValue: HttpResponse<ProfileConfValue>) => profileConfValue.body)
            );
        }
        return of(new ProfileConfValue());
    }
}

export const profileConfValueRoute: Routes = [
    {
        path: 'profile-conf-value',
        component: ProfileConfValueComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProfileConfValues'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'profile-conf-value/:id/view',
        component: ProfileConfValueDetailComponent,
        resolve: {
            profileConfValue: ProfileConfValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProfileConfValues'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'profile-conf-value/new',
        component: ProfileConfValueUpdateComponent,
        resolve: {
            profileConfValue: ProfileConfValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProfileConfValues'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'profile-conf-value/:id/edit',
        component: ProfileConfValueUpdateComponent,
        resolve: {
            profileConfValue: ProfileConfValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProfileConfValues'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const profileConfValuePopupRoute: Routes = [
    {
        path: 'profile-conf-value/:id/delete',
        component: ProfileConfValueDeletePopupComponent,
        resolve: {
            profileConfValue: ProfileConfValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProfileConfValues'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
