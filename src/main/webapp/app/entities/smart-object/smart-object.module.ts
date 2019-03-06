import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CraSharedModule } from 'app/shared';
import { CraAdminModule } from 'app/admin/admin.module';
import {
    SmartObjectComponent,
    SmartObjectDetailComponent,
    SmartObjectUpdateComponent,
    SmartObjectDeletePopupComponent,
    SmartObjectDeleteDialogComponent,
    smartObjectRoute,
    smartObjectPopupRoute
} from './';

const ENTITY_STATES = [...smartObjectRoute, ...smartObjectPopupRoute];

@NgModule({
    imports: [CraSharedModule, CraAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SmartObjectComponent,
        SmartObjectDetailComponent,
        SmartObjectUpdateComponent,
        SmartObjectDeleteDialogComponent,
        SmartObjectDeletePopupComponent
    ],
    entryComponents: [SmartObjectComponent, SmartObjectUpdateComponent, SmartObjectDeleteDialogComponent, SmartObjectDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    exports: [SmartObjectComponent,
        SmartObjectDetailComponent,
        SmartObjectUpdateComponent,
        SmartObjectDeleteDialogComponent,
        SmartObjectDeletePopupComponent]
})
export class CraSmartObjectModule {}
