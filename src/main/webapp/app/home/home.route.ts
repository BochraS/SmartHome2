import { Routes } from '@angular/router';

import { HomeComponent } from './';
import {
    SmartObjectComponent,
    SmartObjectDetailComponent,
    SmartObjectResolve,
    SmartObjectUpdateComponent
} from 'app/entities/smart-object';
import {ProfileComponent, ProfileDetailComponent, ProfileResolve, ProfileUpdateComponent} from 'app/entities/profile';
import {ProfileConfValueComponent} from 'app/entities/profile-conf-value';
import {ConfigComponent} from 'app/entities/config';
import {ConfValueComponent} from 'app/entities/conf-value';
import {UserRouteAccessService} from 'app/core';
export const HOME_ROUTES: Routes = [{
    path: '',
    component: HomeComponent,
    data: {
        authorities: [],
        pageTitle: 'Welcome, Java Hipster!'
    }
},
    {
        path: 'smart-object',
        component: SmartObjectComponent,
    },
    {
        path: 'conf-value',
        component: ConfValueComponent,
    },
    {
        path: 'profile',
        component: ProfileComponent,
    },
    {
        path: 'profileConfValue',
        component: ProfileConfValueComponent,
    },
    {
        path: 'config',
        component: ConfigComponent,
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
    },
    {
        path: 'profile/:id/view',
        component: ProfileDetailComponent,
        resolve: {
            profile: ProfileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Profiles'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'profile/new',
        component: ProfileUpdateComponent,
        resolve: {
            profile: ProfileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Profiles'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'profile/:id/edit',
        component: ProfileUpdateComponent,
        resolve: {
            profile: ProfileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Profiles'
        },
        canActivate: [UserRouteAccessService]
    }
];
