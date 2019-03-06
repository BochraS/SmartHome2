import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CraConfigModule } from './config/config.module';
import { CraConfValueModule } from './conf-value/conf-value.module';
import { CraProfileConfValueModule } from './profile-conf-value/profile-conf-value.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        CraConfigModule,
        CraConfValueModule,
        CraProfileConfValueModule,


        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    exports: [
        CraConfigModule,
        CraConfValueModule,

    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CraEntityModule {}
