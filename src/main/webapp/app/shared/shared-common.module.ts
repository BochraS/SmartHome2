import { NgModule } from '@angular/core';

import { CraSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [CraSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [CraSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class CraSharedCommonModule {}
